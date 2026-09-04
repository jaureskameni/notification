package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import cm.klg.notification.domaine.notification.NotificationType;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

@ExtendWith(MockitoExtension.class)
class ServiceProviderNotificationInboundEventHandlerTest {

  @Mock private CreateNotificationUseCase createNotificationUseCase;
  @Mock private UseCaseExecutor useCaseExecutor;

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

    runCommandWhenExecuted();
    new ApproveServiceProviderInboundEventHandler(
            createNotificationUseCase, useCaseExecutor, new MessagingInboundMapperImpl())
        .handle(event, inboxEvent(event));

    assertNotification(
        NotificationType.SERVICE_PROVIDER_APPROVED, recipientId, actorId, serviceProviderId);
  }

  @Test
  void shouldCreateCreatedNotificationTest() {
    UUID userId = UUID.randomUUID();
    UUID serviceProviderId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now().minusMinutes(5);
    ServiceProviderServiceProviderCreatedEventDTO event =
        new ServiceProviderServiceProviderCreatedEventDTO()
            .serviceProviderId(serviceProviderId)
            .userId(userId)
            .createdAt(createdAt);

    runCommandWhenExecuted();
    new CreateServiceProviderInboundEventHandler(
            createNotificationUseCase, useCaseExecutor, new MessagingInboundMapperImpl())
        .handle(event, inboxEvent(event));

    assertNotification(
        NotificationType.SERVICE_PROVIDER_CREATED, userId, userId, serviceProviderId);
  }

  @Test
  void shouldCreateRejectedNotificationWithReasonTest() {
    UUID recipientId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID serviceProviderId = UUID.randomUUID();
    LocalDateTime rejectedAt = LocalDateTime.now().minusMinutes(5);
    ServiceProviderServiceProviderRejectedEventDTO event =
        new ServiceProviderServiceProviderRejectedEventDTO()
            .serviceProviderId(serviceProviderId)
            .userId(recipientId)
            .rejectedBy(actorId)
            .reason("Document invalide")
            .rejectedAt(rejectedAt);

    runCommandWhenExecuted();
    new RejectServiceProviderInboundEventHandler(
            createNotificationUseCase, useCaseExecutor, new MessagingInboundMapperImpl())
        .handle(event, inboxEvent(event));

    assertNotification(
        NotificationType.SERVICE_PROVIDER_REJECTED, recipientId, actorId, serviceProviderId);
  }

  private void assertNotification(
      NotificationType notificationType, UUID recipientId, UUID actorId, UUID serviceProviderId) {
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
            });
  }

  private void runCommandWhenExecuted() {
    doAnswer(
            invocation -> {
              invocation.<Runnable>getArgument(0).run();
              return null;
            })
        .when(useCaseExecutor)
        .runCommand(any(Runnable.class));
  }

  private static InboxEventCommand inboxEvent(Object event) {
    return new InboxEventCommand(UUID.randomUUID(), "serviceProvider", "event", "key", event);
  }
}
