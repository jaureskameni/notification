package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamPhoneNumberDTO;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.notification.application.usecase.CreateNewUserUseCase;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import cm.klg.notification.domaine.notification.NotificationType;
import cm.klg.notification.domaine.notification.ReferenceType;
import cm.klg.notification.domaine.user.UserId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

class MessagingInboundMapperTest {

  private MessagingInboundMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = Mappers.getMapper(MessagingInboundMapper.class);
  }

  @Test
  void shouldMapToCreateUserCommandTest() {
    var userId = UUID.randomUUID();
    var identityId = UUID.randomUUID();
    var createdAt = LocalDateTime.now();

    var event =
        new UamUserCreatedEventDTO()
            .id(userId)
            .identityId(identityId)
            .firstname("John")
            .lastname("Doe")
            .email("john.doe@example.com")
            .phoneNumber(new UamPhoneNumberDTO().countryCode("+237").number("699999999"))
            .createdAt(createdAt);

    var command = mapper.toCreateUserCommand(event);

    assertThat(command)
        .extracting(
            CreateNewUserUseCase.CreateNewUserCommand::id,
            CreateNewUserUseCase.CreateNewUserCommand::identityId,
            CreateNewUserUseCase.CreateNewUserCommand::countryCode,
            CreateNewUserUseCase.CreateNewUserCommand::phoneNumber)
        .containsExactly(userId, identityId, "+237", "699999999");
    assertThat(command.profile())
        .extracting(
            CreateNewUserUseCase.CreateNewUserCommand.UserProfileCommand::firstname,
            CreateNewUserUseCase.CreateNewUserCommand.UserProfileCommand::lastname,
            CreateNewUserUseCase.CreateNewUserCommand.UserProfileCommand::email)
        .containsExactly("John", "Doe", "john.doe@example.com");
  }

  @Test
  void shouldMapToCreatedNotificationCommandTest() {
    var userId = UUID.randomUUID();
    var serviceProviderId = UUID.randomUUID();

    var event =
        new ServiceProviderServiceProviderCreatedEventDTO()
            .userId(userId)
            .serviceProviderId(serviceProviderId);

    var command = mapper.toCreatedNotificationCommand(event);

    assertThat(command)
        .extracting(
            CreateNotificationUseCase.CreateNotificationCommand::notificationType,
            CreateNotificationUseCase.CreateNotificationCommand::referenceType)
        .containsExactly(NotificationType.SERVICE_PROVIDER_CREATED, ReferenceType.SERVICE_PROVIDER);
    assertThat(command.recipientId()).isEqualTo(UserId.from(userId));
    assertThat(command.actorId()).isEqualTo(UserId.from(userId));
  }

  @Test
  void shouldMapToApprovedNotificationCommandTest() {
    var userId = UUID.randomUUID();
    var approvedBy = UUID.randomUUID();
    var serviceProviderId = UUID.randomUUID();

    var event =
        new ServiceProviderServiceProviderApprovedEventDTO()
            .userId(userId)
            .approvedBy(approvedBy)
            .serviceProviderId(serviceProviderId);

    var command = mapper.toApprovedNotificationCommand(event);

    assertThat(command.notificationType()).isEqualTo(NotificationType.SERVICE_PROVIDER_APPROVED);
    assertThat(command.recipientId()).isEqualTo(UserId.from(userId));
    assertThat(command.actorId()).isEqualTo(UserId.from(approvedBy));
  }

  @Test
  void shouldMapToRejectedNotificationCommandTest() {
    var userId = UUID.randomUUID();
    var rejectedBy = UUID.randomUUID();
    var serviceProviderId = UUID.randomUUID();

    var event =
        new ServiceProviderServiceProviderRejectedEventDTO()
            .userId(userId)
            .rejectedBy(rejectedBy)
            .serviceProviderId(serviceProviderId);

    var command = mapper.toRejectedNotificationCommand(event);

    assertThat(command.notificationType()).isEqualTo(NotificationType.SERVICE_PROVIDER_REJECTED);
    assertThat(command.recipientId()).isEqualTo(UserId.from(userId));
    assertThat(command.actorId()).isEqualTo(UserId.from(rejectedBy));
  }

  @Test
  void shouldMapToCreateUserCommandWithNullEmailTest() {
    var userId = UUID.randomUUID();
    var identityId = UUID.randomUUID();

    var event =
        new UamUserCreatedEventDTO()
            .id(userId)
            .identityId(identityId)
            .firstname("Jane")
            .lastname("Smith")
            .email(null)
            .phoneNumber(new UamPhoneNumberDTO().countryCode("+1").number("5551234567"))
            .createdAt(LocalDateTime.now());

    var command = mapper.toCreateUserCommand(event);

    assertThat(command.profile().email()).isNull();
    assertThat(command.profile().firstname()).isEqualTo("Jane");
  }
}
