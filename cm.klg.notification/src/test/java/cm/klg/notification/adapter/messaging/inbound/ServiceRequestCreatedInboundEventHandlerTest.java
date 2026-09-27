package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestCreatedEventDTO;

@ExtendWith(MockitoExtension.class)
class ServiceRequestCreatedInboundEventHandlerTest {

  @Mock private CreateNotificationUseCase createNotificationUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private ServiceRequestCreatedInboundEventHandler handler;

  @Test
  void shouldExposeServiceRequestCreatedEventTypeTest() {
    assertThat(handler.handledEventType())
        .isEqualTo(ServiceRequestDomainEventType.SERVICE_REQUEST_CREATED.getValue());
  }

  @Test
  void shouldExposeServiceRequestCreatedEventDTODataTypeTest() {
    assertThat(handler.payloadType()).isEqualTo(ServiceRequestServiceRequestCreatedEventDTO.class);
  }

  @Test
  void shouldHandleServiceRequestCreatedEventTest() {
    ServiceRequestServiceRequestCreatedEventDTO event =
        new ServiceRequestServiceRequestCreatedEventDTO();
    event.setId(UUID.randomUUID());
    event.setUserId(UUID.randomUUID());
    event.setProviderId(UUID.randomUUID());
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    handler.handle(inboxEventCommand);

    verify(messagingInboundMapper).toServiceRequestCreatedNotificationCommand(event);
    verify(createNotificationUseCase).execute(any());
  }

  @Test
  void shouldThrowExceptionWhenHandlingFailsTest() {
    ServiceRequestServiceRequestCreatedEventDTO event =
        new ServiceRequestServiceRequestCreatedEventDTO();
    event.setId(UUID.randomUUID());
    event.setUserId(UUID.randomUUID());
    event.setProviderId(UUID.randomUUID());
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    var command =
        new CreateNotificationUseCase.CreateNotificationCommand(
            cm.klg.notification.domaine.user.UserId.from(event.getProviderId()),
            cm.klg.notification.domaine.user.UserId.from(event.getUserId()),
            cm.klg.notification.domaine.notification.NotificationType.SERVICE_REQUEST_CREATED,
            cm.klg.notification.domaine.notification.ReferenceType.SERVICE_REQUEST,
            cm.klg.notification.domaine.notification.NotificationReferenceId.from(event.getId()));

    when(messagingInboundMapper.toServiceRequestCreatedNotificationCommand(event))
        .thenReturn(command);
    doThrow(new RuntimeException("Error")).when(createNotificationUseCase).execute(command);

    assertThatThrownBy(() -> handler.handle(inboxEventCommand))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Error");
  }

  private static InboxEventCommand<ServiceRequestServiceRequestCreatedEventDTO>
      givenInboxEventCommand(ServiceRequestServiceRequestCreatedEventDTO event) {
    return new InboxEventCommand<>(
        UUID.randomUUID(),
        "service-request",
        ServiceRequestDomainEventType.SERVICE_REQUEST_CREATED.getValue(),
        String.valueOf(event.getId()),
        event);
  }
}
