package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestRejectedEventDTO;

@ExtendWith(MockitoExtension.class)
class ServiceRequestRejectedInboundEventHandlerTest {

  @Mock private CreateNotificationUseCase createNotificationUseCase;
  @Mock private UseCaseExecutor useCaseExecutor;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks private ServiceRequestRejectedInboundEventHandler handler;

  @Test
  void shouldExposeServiceRequestRejectedEventTypeTest() {
    assertThat(handler.getEventType())
        .isEqualTo(ServiceRequestDomainEventType.SERVICE_REQUEST_REJECTED.getValue());
  }

  @Test
  void shouldExposeServiceRequestRejectedEventDTODataTypeTest() {
    assertThat(handler.getDataType()).isEqualTo(ServiceRequestServiceRequestRejectedEventDTO.class);
  }

  @Test
  void shouldHandleServiceRequestRejectedEventTest() {
    ServiceRequestServiceRequestRejectedEventDTO event =
        new ServiceRequestServiceRequestRejectedEventDTO();
    event.setId(UUID.randomUUID());
    event.setUserId(UUID.randomUUID());
    event.setProviderId(UUID.randomUUID());
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    doAnswer(
            invocation -> {
              invocation.getArgument(0, Runnable.class).run();
              return null;
            })
        .when(useCaseExecutor)
        .runCommand(any());

    handler.handle(event, inboxEventCommand);

    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void shouldThrowExceptionWhenHandlingFailsTest() {
    ServiceRequestServiceRequestRejectedEventDTO event =
        new ServiceRequestServiceRequestRejectedEventDTO();
    event.setId(UUID.randomUUID());
    event.setUserId(UUID.randomUUID());
    event.setProviderId(UUID.randomUUID());
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    doThrow(new RuntimeException("Error")).when(useCaseExecutor).runCommand(any());

    try {
      handler.handle(event, inboxEventCommand);
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).isEqualTo("Error");
    }

    verify(useCaseExecutor).runCommand(any());
  }

  private static InboxEventCommand givenInboxEventCommand(
      ServiceRequestServiceRequestRejectedEventDTO event) {
    return new InboxEventCommand(
        UUID.randomUUID(),
        "service-request",
        ServiceRequestDomainEventType.SERVICE_REQUEST_REJECTED.getValue(),
        String.valueOf(event.getId()),
        event);
  }
}
