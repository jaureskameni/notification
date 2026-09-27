package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestCreatedEventDTO;

public record ServiceRequestCreatedInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceRequestServiceRequestCreatedEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceRequestDomainEventType.SERVICE_REQUEST_CREATED.getValue();
  }

  @Override
  public Class<ServiceRequestServiceRequestCreatedEventDTO> payloadType() {
    return ServiceRequestServiceRequestCreatedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceRequestServiceRequestCreatedEventDTO> inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toServiceRequestCreatedNotificationCommand(
            inboxEventCommand.data()));
  }
}
