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
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;

@ExtendWith(MockitoExtension.class)
class CreateServiceProviderInboundEventHandlerTest {

  @Mock private CreateNotificationUseCase createNotificationUseCase;
  @Mock private UseCaseExecutor useCaseExecutor;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks
  private CreateServiceProviderInboundEventHandler createServiceProviderInboundEventHandler;

  @Test
  void shouldExposeServiceProviderCreatedEventTypeTest() {
    String eventType = createServiceProviderInboundEventHandler.getEventType();

    assertThat(eventType)
        .isEqualTo(ServiceProviderDomainEventType.SERVICE_PROVIDER_CREATED.getValue());
  }

  @Test
  void shouldExposeServiceProviderCreatedEventDTODataTypeTest() {
    Class<?> dataType = createServiceProviderInboundEventHandler.getDataType();

    assertThat(dataType).isEqualTo(ServiceProviderServiceProviderCreatedEventDTO.class);
  }

  @Test
  void shouldHandleServiceProviderCreatedEventTest() {
    ServiceProviderServiceProviderCreatedEventDTO event =
        new ServiceProviderServiceProviderCreatedEventDTO();
    event.setUserId(UUID.randomUUID());
    event.setServiceProviderId(UUID.randomUUID());
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    doAnswer(
            invocation -> {
              invocation.getArgument(0, Runnable.class).run();
              return null;
            })
        .when(useCaseExecutor)
        .runCommand(any());

    createServiceProviderInboundEventHandler.handle(event, inboxEventCommand);

    verify(useCaseExecutor).runCommand(any());
  }

  @Test
  void shouldThrowExceptionWhenHandlingFailsTest() {
    ServiceProviderServiceProviderCreatedEventDTO event =
        new ServiceProviderServiceProviderCreatedEventDTO();
    event.setUserId(UUID.randomUUID());
    event.setServiceProviderId(UUID.randomUUID());
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    doThrow(new RuntimeException("Error")).when(useCaseExecutor).runCommand(any());

    try {
      createServiceProviderInboundEventHandler.handle(event, inboxEventCommand);
    } catch (RuntimeException e) {
      assertThat(e.getMessage()).isEqualTo("Error");
    }

    verify(useCaseExecutor).runCommand(any());
  }

  private static InboxEventCommand givenInboxEventCommand(
      ServiceProviderServiceProviderCreatedEventDTO event) {
    return new InboxEventCommand(
        UUID.randomUUID(),
        "service-provider",
        ServiceProviderDomainEventType.SERVICE_PROVIDER_CREATED.getValue(),
        String.valueOf(event.getServiceProviderId()),
        event);
  }
}
