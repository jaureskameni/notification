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
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;

@ExtendWith(MockitoExtension.class)
class ApproveServiceProviderInboundEventHandlerTest {

  @Mock private CreateNotificationUseCase createNotificationUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private ApproveServiceProviderInboundEventHandler handler;

  @Test
  void shouldExposeEventTypeAndDataType() {
    assertThat(handler.handledEventType())
        .isEqualTo(ServiceProviderDomainEventType.SERVICE_PROVIDER_APPROVED.getValue());
    assertThat(handler.payloadType())
        .isEqualTo(ServiceProviderServiceProviderApprovedEventDTO.class);
  }

  @Test
  void shouldHandleApprovedEventTest() {
    var event = givenApprovedEvent();
    var inboxEventCommand = givenInboxEventCommand(event);

    handler.handle(inboxEventCommand);

    verify(messagingInboundMapper).toApprovedNotificationCommand(event);
    verify(createNotificationUseCase).execute(any());
  }

  @Test
  void shouldPropagateExceptionRaisedByUseCaseTest() {
    var event = givenApprovedEvent();
    var inboxEventCommand = givenInboxEventCommand(event);
    var command = givenApprovedNotificationCommand();

    when(messagingInboundMapper.toApprovedNotificationCommand(event)).thenReturn(command);
    doThrow(new IllegalStateException("boom")).when(createNotificationUseCase).execute(command);

    assertThatThrownBy(() -> handler.handle(inboxEventCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("boom");
  }

  @Test
  void shouldHandleApprovedEventThroughFullFlowTest() {
    var event = givenApprovedEvent();
    var inboxEventCommand = givenInboxEventCommand(event);
    var mockUseCase = mock(CreateNotificationUseCase.class);

    var realFlowHandler =
        new ApproveServiceProviderInboundEventHandler(
            mockUseCase, new MessagingInboundMapperImpl());

    realFlowHandler.handle(inboxEventCommand);

    verify(mockUseCase).execute(any(CreateNotificationUseCase.CreateNotificationCommand.class));
  }

  private static ServiceProviderServiceProviderApprovedEventDTO givenApprovedEvent() {
    return new ServiceProviderServiceProviderApprovedEventDTO()
        .userId(UUID.randomUUID())
        .approvedBy(UUID.randomUUID())
        .serviceProviderId(UUID.randomUUID());
  }

  private static InboxEventCommand<ServiceProviderServiceProviderApprovedEventDTO>
      givenInboxEventCommand(ServiceProviderServiceProviderApprovedEventDTO event) {
    return new InboxEventCommand<>(
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
}
