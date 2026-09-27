package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;

@ExtendWith(MockitoExtension.class)
class CreateServiceProviderInboundEventHandlerTest {

  @Mock private CreateNotificationUseCase createNotificationUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;

  @InjectMocks
  private CreateServiceProviderInboundEventHandler createServiceProviderInboundEventHandler;

  @Test
  void shouldExposeServiceProviderCreatedEventTypeTest() {
    assertThat(createServiceProviderInboundEventHandler.handledEventType())
        .isEqualTo(ServiceProviderDomainEventType.SERVICE_PROVIDER_CREATED.getValue());
  }

  @Test
  void shouldExposeServiceProviderCreatedEventDTODataTypeTest() {
    assertThat(createServiceProviderInboundEventHandler.payloadType())
        .isEqualTo(ServiceProviderServiceProviderCreatedEventDTO.class);
  }

  @Test
  void shouldHandleServiceProviderCreatedEventTest() {
    ServiceProviderServiceProviderCreatedEventDTO event =
        new ServiceProviderServiceProviderCreatedEventDTO();
    event.setUserId(UUID.randomUUID());
    event.setServiceProviderId(UUID.randomUUID());
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    createServiceProviderInboundEventHandler.handle(inboxEventCommand);

    verify(messagingInboundMapper).toCreatedNotificationCommand(event);
    verify(createNotificationUseCase).execute(any());
  }

  @Test
  void shouldThrowExceptionWhenHandlingFailsTest() {
    ServiceProviderServiceProviderCreatedEventDTO event =
        new ServiceProviderServiceProviderCreatedEventDTO();
    event.setUserId(UUID.randomUUID());
    event.setServiceProviderId(UUID.randomUUID());
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);
    var command =
        new CreateNotificationUseCase.CreateNotificationCommand(
            UserId.from(UUID.randomUUID()),
            UserId.from(UUID.randomUUID()),
            cm.klg.notification.domaine.notification.NotificationType.SERVICE_PROVIDER_CREATED,
            cm.klg.notification.domaine.notification.ReferenceType.SERVICE_PROVIDER,
            cm.klg.notification.domaine.notification.NotificationReferenceId.from(
                UUID.randomUUID()));

    when(messagingInboundMapper.toCreatedNotificationCommand(event)).thenReturn(command);
    doThrow(new RuntimeException("Error")).when(createNotificationUseCase).execute(command);

    assertThatThrownBy(() -> createServiceProviderInboundEventHandler.handle(inboxEventCommand))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Error");
  }

  private static InboxEventCommand<ServiceProviderServiceProviderCreatedEventDTO>
      givenInboxEventCommand(ServiceProviderServiceProviderCreatedEventDTO event) {
    return new InboxEventCommand<>(
        UUID.randomUUID(),
        "service-provider",
        ServiceProviderDomainEventType.SERVICE_PROVIDER_CREATED.getValue(),
        String.valueOf(event.getServiceProviderId()),
        event);
  }
}
