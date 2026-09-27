package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

@ExtendWith(MockitoExtension.class)
class RejectServiceProviderInboundEventHandlerTest {

  @Mock private CreateNotificationUseCase createNotificationUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private RejectServiceProviderInboundEventHandler handler;

  @Test
  void shouldExposeEventTypeAndDataType() {
    assertThat(handler.handledEventType())
        .isEqualTo(ServiceProviderDomainEventType.SERVICE_PROVIDER_REJECTED.getValue());
    assertThat(handler.payloadType())
        .isEqualTo(ServiceProviderServiceProviderRejectedEventDTO.class);
  }

  @Test
  void shouldHandleRejectedEventTest() {
    var event = givenRejectedEvent();
    var inboxEventCommand = givenInboxEventCommand(event);

    handler.handle(inboxEventCommand);

    verify(messagingInboundMapper).toRejectedNotificationCommand(event);
    verify(createNotificationUseCase).execute(any());
  }

  @Test
  void shouldPropagateExceptionRaisedByUseCaseTest() {
    var event = givenRejectedEvent();
    var inboxEventCommand = givenInboxEventCommand(event);
    var command = givenRejectedNotificationCommand();

    when(messagingInboundMapper.toRejectedNotificationCommand(event)).thenReturn(command);
    doThrow(new IllegalStateException("boom")).when(createNotificationUseCase).execute(command);

    assertThatThrownBy(() -> handler.handle(inboxEventCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("boom");
  }

  @Test
  void shouldHandleRejectedEventThroughFullFlowTest() {
    var event = givenRejectedEvent();
    var inboxEventCommand = givenInboxEventCommand(event);
    var mockUseCase = mock(CreateNotificationUseCase.class);

    var realFlowHandler =
        new RejectServiceProviderInboundEventHandler(mockUseCase, new MessagingInboundMapperImpl());

    realFlowHandler.handle(inboxEventCommand);

    verify(mockUseCase).execute(any(CreateNotificationUseCase.CreateNotificationCommand.class));
  }

  private static ServiceProviderServiceProviderRejectedEventDTO givenRejectedEvent() {
    return new ServiceProviderServiceProviderRejectedEventDTO()
        .userId(UUID.randomUUID())
        .rejectedBy(UUID.randomUUID())
        .serviceProviderId(UUID.randomUUID());
  }

  private static InboxEventCommand<ServiceProviderServiceProviderRejectedEventDTO>
      givenInboxEventCommand(ServiceProviderServiceProviderRejectedEventDTO event) {
    return new InboxEventCommand<>(
        UUID.randomUUID(),
        "service-provider",
        ServiceProviderDomainEventType.SERVICE_PROVIDER_REJECTED.getValue(),
        String.valueOf(event.getServiceProviderId()),
        event);
  }

  private static CreateNotificationUseCase.CreateNotificationCommand
      givenRejectedNotificationCommand() {
    return new CreateNotificationUseCase.CreateNotificationCommand(
        UserId.from(UUID.randomUUID()),
        UserId.from(UUID.randomUUID()),
        cm.klg.notification.domaine.notification.NotificationType.SERVICE_PROVIDER_REJECTED,
        cm.klg.notification.domaine.notification.ReferenceType.SERVICE_PROVIDER,
        cm.klg.notification.domaine.notification.NotificationReferenceId.from(UUID.randomUUID()));
  }
}
