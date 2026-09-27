package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamPhoneNumberDTO;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserUpdatedEventDTO;
import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.application.usecase.UpdateUserUseCase;
import cm.klg.notification.domaine.user.User;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserInboundEventHandlerTest {

  @Mock private UpdateUserUseCase updateUserUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private UpdateUserInboundEventHandler handler;

  @Test
  void shouldExposeUserUpdatedEventTypeTest() {
    assertThat(handler.handledEventType()).isEqualTo(UamDomainEventType.USER_UPDATED.getValue());
  }

  @Test
  void shouldExposeUamUserUpdatedEventDataTypeTest() {
    assertThat(handler.payloadType()).isEqualTo(UamUserUpdatedEventDTO.class);
  }

  @Test
  void shouldUpdateUserFromEventDataTest() {
    UUID userId = UUID.randomUUID();
    LocalDateTime updatedAt = LocalDateTime.now().minusMinutes(10);
    UamUserUpdatedEventDTO event =
        new UamUserUpdatedEventDTO()
            .id(userId)
            .lastname("Doe")
            .firstname(" John ")
            .email("john.doe@example.com")
            .phoneNumber(new UamPhoneNumberDTO().countryCode("+237").number("699999999"))
            .updatedAt(updatedAt);
    InboxEventCommand<UamUserUpdatedEventDTO> inboxEventCommand = givenInboxEventCommand(event);

    handler.handle(inboxEventCommand);

    verify(messagingInboundMapper).toUpdateUserCommand(event);
    verify(updateUserUseCase).execute(any());
  }

  @Test
  void shouldPropagateExceptionRaisedByUseCaseTest() {
    UamUserUpdatedEventDTO event = givenUserUpdatedEvent();
    InboxEventCommand<UamUserUpdatedEventDTO> inboxEventCommand = givenInboxEventCommand(event);
    UpdateUserUseCase.UpdateUserCommand command = givenUpdateUserCommand();

    when(messagingInboundMapper.toUpdateUserCommand(event)).thenReturn(command);
    doThrow(new IllegalStateException("boom")).when(updateUserUseCase).execute(command);

    assertThatThrownBy(() -> handler.handle(inboxEventCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("boom");
  }

  @Test
  void shouldUpdateUserFromEventDataThroughFullFlowTest() {
    UUID userId = UUID.randomUUID();
    LocalDateTime updatedAt = LocalDateTime.now().minusMinutes(10);
    UamUserUpdatedEventDTO event =
        new UamUserUpdatedEventDTO()
            .id(userId)
            .lastname("Doe")
            .firstname(" John ")
            .email("john.doe@example.com")
            .phoneNumber(new UamPhoneNumberDTO().countryCode("+237").number("699999999"))
            .updatedAt(updatedAt);
    InboxEventCommand<UamUserUpdatedEventDTO> inboxEventCommand = givenInboxEventCommand(event);

    UserRepository userRepository = mock(UserRepository.class);
    UpdateUserInboundEventHandler realFlowHandler =
        new UpdateUserInboundEventHandler(
            new UpdateUserUseCase(userRepository), new MessagingInboundMapperImpl());

    realFlowHandler.handle(inboxEventCommand);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).update(userCaptor.capture());

    assertThat(userCaptor.getValue())
        .satisfies(
            user -> {
              assertThat(user.getId().value()).isEqualTo(userId);
              assertThat(user.getLastname().value()).isEqualTo("Doe");
              assertThat(user.getFirstname()).isNotNull();
              assertThat(user.getFirstname().value()).isEqualTo("John");
              assertThat(user.getEmail()).isNotNull();
              assertThat(user.getEmail().value()).isEqualTo("john.doe@example.com");
              assertThat(user.getPhoneNumber().countryCode()).isEqualTo("+237");
              assertThat(user.getPhoneNumber().number()).isEqualTo("699999999");
              assertThat(user.getCreatedAt().value()).isEqualTo(updatedAt);
            });
  }

  private static UamUserUpdatedEventDTO givenUserUpdatedEvent() {
    return new UamUserUpdatedEventDTO()
        .id(UUID.randomUUID())
        .lastname("Doe")
        .firstname(" John ")
        .email("john.doe@example.com")
        .phoneNumber(new UamPhoneNumberDTO().countryCode("+237").number("699999999"))
        .updatedAt(LocalDateTime.now().minusMinutes(10));
  }

  private static InboxEventCommand<UamUserUpdatedEventDTO> givenInboxEventCommand(
      UamUserUpdatedEventDTO event) {
    return new InboxEventCommand<>(
        UUID.randomUUID(),
        "user",
        UamDomainEventType.USER_UPDATED.getValue(),
        String.valueOf(event.getId()),
        event);
  }

  private static UpdateUserUseCase.UpdateUserCommand givenUpdateUserCommand() {
    return new UpdateUserUseCase.UpdateUserCommand(
        UUID.randomUUID(),
        new UpdateUserUseCase.UpdateUserCommand.UserProfileCommand(
            "Doe", " John ", "john.doe@example.com"),
        "+237",
        "699999999",
        LocalDateTime.now().minusDays(1));
  }
}
