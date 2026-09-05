package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import cm.klg.notification.domaine.user.UserId;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;

@ExtendWith(MockitoExtension.class)
class ApproveServiceProviderInboundEventHandlerTest {

  @Mock private CreateNotificationUseCase createNotificationUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;
  @Mock private UseCaseExecutor useCaseExecutor;

  @InjectMocks private ApproveServiceProviderInboundEventHandler handler;

  @Test
  void shouldExposeEventTypeAndDataType() {
    assertThat(handler.getEventType())
        .isEqualTo(ServiceProviderDomainEventType.SERVICE_PROVIDER_APPROVED.getValue());
    assertThat(handler.getDataType())
        .isEqualTo(ServiceProviderServiceProviderApprovedEventDTO.class);
  }

  @Test
  void shouldNotExecuteCommandWhenExecutorDoesNotRunIt() {
    var event = givenApprovedEvent();
    var inboxEventCommand = givenInboxEventCommand(event);

    handler.handle(event, inboxEventCommand);

    verify(messagingInboundMapper, never()).toApprovedNotificationCommand(event);
    verify(createNotificationUseCase, never())
        .execute(any(CreateNotificationUseCase.CreateNotificationCommand.class));
  }

  @Test
  void shouldPropagateExceptionRaisedByUseCase() {
    var event = givenApprovedEvent();
    var inboxEventCommand = givenInboxEventCommand(event);
    var command = givenApprovedNotificationCommand();

    when(messagingInboundMapper.toApprovedNotificationCommand(event)).thenReturn(command);
    doThrow(new IllegalStateException("boom")).when(createNotificationUseCase).execute(command);
    runCommandWhenExecuted(useCaseExecutor);

    assertThatThrownBy(() -> handler.handle(event, inboxEventCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("boom");
  }

  @Test
  void shouldHandleApprovedEventThroughFullFlow() {
    var event = givenApprovedEvent();
    var inboxEventCommand = givenInboxEventCommand(event);
    var mockUseCase = mock(CreateNotificationUseCase.class);

    var realFlowHandler =
        new ApproveServiceProviderInboundEventHandler(
            mockUseCase, useCaseExecutor, new MessagingInboundMapperImpl());
    runCommandWhenExecuted(useCaseExecutor);

    realFlowHandler.handle(event, inboxEventCommand);

    verify(mockUseCase).execute(any(CreateNotificationUseCase.CreateNotificationCommand.class));
  }

  private static ServiceProviderServiceProviderApprovedEventDTO givenApprovedEvent() {
    return new ServiceProviderServiceProviderApprovedEventDTO()
        .userId(UUID.randomUUID())
        .approvedBy(UUID.randomUUID())
        .serviceProviderId(UUID.randomUUID());
  }

  private static InboxEventCommand givenInboxEventCommand(
      ServiceProviderServiceProviderApprovedEventDTO event) {
    return new InboxEventCommand(
        UUID.randomUUID(),
        "service-provider",
        ServiceProviderDomainEventType.SERVICE_PROVIDER_APPROVED.getValue(),
        String.valueOf(event.getServiceProviderId()),
        event);
  }

  private static CreateNotificationUseCase.CreateNotificationCommand
      givenApprovedNotificationCommand() {
    return new CreateNotificationUseCase.CreateNotificationCommand(
        UserId.from(UUID.randomUUID()),
        UserId.from(UUID.randomUUID()),
        cm.klg.notification.domaine.notification.NotificationType.SERVICE_PROVIDER_APPROVED,
        cm.klg.notification.domaine.notification.ReferenceType.SERVICE_PROVIDER,
        cm.klg.notification.domaine.notification.NotificationReferenceId.from(UUID.randomUUID()));
  }

  private static void runCommandWhenExecuted(UseCaseExecutor executor) {
    doAnswer(
            invocation -> {
              invocation.<Runnable>getArgument(0).run();
              return null;
            })
        .when(executor)
        .runCommand(any(Runnable.class));
  }
}
