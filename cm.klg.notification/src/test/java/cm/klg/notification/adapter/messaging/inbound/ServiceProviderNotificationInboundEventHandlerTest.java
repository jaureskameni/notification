package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import cm.klg.notification.domaine.notification.NotificationType;
import cm.klg.notification.domaine.notification.RejectionReason;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderRejectionReason;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderProfileImageApprovedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderProfileImageRejectedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

@ExtendWith(MockitoExtension.class)
class ServiceProviderNotificationInboundEventHandlerTest {

  @Mock private CreateNotificationUseCase createNotificationUseCase;

  @Test
  void shouldConsumeProfileImageApprovedEvent() {
    var handler =
        new ApproveServiceProviderProfileImageInboundEventHandler(
            createNotificationUseCase, new MessagingInboundMapperImpl());

    assertThat(handler.handledEventType())
        .isEqualTo(
            ServiceProviderDomainEventType.SERVICE_PROVIDER_PROFILE_IMAGE_APPROVED.getValue());
    assertThat(handler.payloadType())
        .isEqualTo(ServiceProviderServiceProviderProfileImageApprovedEventDTO.class);
  }

  @Test
  void shouldConsumeProfileImageRejectedEvent() {
    var handler =
        new RejectServiceProviderProfileImageInboundEventHandler(
            createNotificationUseCase, new MessagingInboundMapperImpl());

    assertThat(handler.handledEventType())
        .isEqualTo(
            ServiceProviderDomainEventType.SERVICE_PROVIDER_PROFILE_IMAGE_REJECTED.getValue());
    assertThat(handler.payloadType())
        .isEqualTo(ServiceProviderServiceProviderProfileImageRejectedEventDTO.class);
  }

  @Test
  void shouldCreateApprovedNotificationTest() {
    UUID recipientId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID serviceProviderId = UUID.randomUUID();
    LocalDateTime approvedAt = LocalDateTime.now().minusMinutes(5);
    ServiceProviderServiceProviderApprovedEventDTO event =
        new ServiceProviderServiceProviderApprovedEventDTO()
            .serviceProviderId(serviceProviderId)
            .userId(recipientId)
            .approvedBy(actorId)
            .approvedAt(approvedAt);

    new ApproveServiceProviderInboundEventHandler(
            createNotificationUseCase, new MessagingInboundMapperImpl())
        .handle(inboxEvent(event));

    assertNotification(
        NotificationType.SERVICE_PROVIDER_APPROVED, recipientId, actorId, serviceProviderId);
  }

  @Test
  void shouldCreateRejectedNotificationTest() {
    UUID recipientId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID serviceProviderId = UUID.randomUUID();
    LocalDateTime rejectedAt = LocalDateTime.now().minusMinutes(5);
    ServiceProviderServiceProviderRejectedEventDTO event =
        new ServiceProviderServiceProviderRejectedEventDTO()
            .serviceProviderId(serviceProviderId)
            .userId(recipientId)
            .rejectedBy(actorId)
            .reason(ServiceProviderRejectionReason.CNI_EXPIRED)
            .rejectedAt(rejectedAt);

    new RejectServiceProviderInboundEventHandler(
            createNotificationUseCase, new MessagingInboundMapperImpl())
        .handle(inboxEvent(event));

    assertNotification(
        NotificationType.SERVICE_PROVIDER_REJECTED,
        recipientId,
        actorId,
        serviceProviderId,
        "CNI_EXPIRED");
  }

  @Test
  void shouldCreateProfileImageApprovedNotificationTest() {
    UUID recipientId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID serviceProviderId = UUID.randomUUID();
    ServiceProviderServiceProviderProfileImageApprovedEventDTO event =
        new ServiceProviderServiceProviderProfileImageApprovedEventDTO()
            .serviceProviderId(serviceProviderId)
            .userId(recipientId)
            .approvedBy(actorId);

    new ApproveServiceProviderProfileImageInboundEventHandler(
            createNotificationUseCase, new MessagingInboundMapperImpl())
        .handle(inboxEvent(event));

    assertNotification(
        NotificationType.SERVICE_PROVIDER_PROFILE_IMAGE_APPROVED,
        recipientId,
        actorId,
        serviceProviderId);
  }

  @Test
  void shouldCreateProfileImageRejectedNotificationTest() {
    UUID recipientId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID serviceProviderId = UUID.randomUUID();
    ServiceProviderServiceProviderProfileImageRejectedEventDTO event =
        new ServiceProviderServiceProviderProfileImageRejectedEventDTO()
            .serviceProviderId(serviceProviderId)
            .userId(recipientId)
            .rejectedBy(actorId)
            .reason(ServiceProviderRejectionReason.PROFILE_IMAGE_UNCLEAR);

    new RejectServiceProviderProfileImageInboundEventHandler(
            createNotificationUseCase, new MessagingInboundMapperImpl())
        .handle(inboxEvent(event));

    assertNotification(
        NotificationType.SERVICE_PROVIDER_PROFILE_IMAGE_REJECTED,
        recipientId,
        actorId,
        serviceProviderId,
        "PROFILE_IMAGE_UNCLEAR");
  }

  private void assertNotification(
      NotificationType notificationType, UUID recipientId, UUID actorId, UUID serviceProviderId) {
    assertNotification(notificationType, recipientId, actorId, serviceProviderId, null);
  }

  private void assertNotification(
      NotificationType notificationType,
      UUID recipientId,
      UUID actorId,
      UUID serviceProviderId,
      String reason) {
    ArgumentCaptor<CreateNotificationUseCase.CreateNotificationCommand> commandCaptor =
        ArgumentCaptor.forClass(CreateNotificationUseCase.CreateNotificationCommand.class);
    verify(createNotificationUseCase).execute(commandCaptor.capture());
    assertThat(commandCaptor.getValue())
        .satisfies(
            command -> {
              assertThat(command.notificationType()).isEqualTo(notificationType);
              assertThat(command.recipientId().value()).isEqualTo(recipientId);
              assertThat(command.actorId().value()).isEqualTo(actorId);
              assertThat(command.referenceId().value()).isEqualTo(serviceProviderId);
              assertThat(command.reason())
                  .isEqualTo(reason == null ? RejectionReason.NULL : RejectionReason.from(reason));
            });
  }

  private static <T> InboxEventCommand<T> inboxEvent(T event) {
    return new InboxEventCommand<>(UUID.randomUUID(), "serviceProvider", "event", "key", event);
  }
}
