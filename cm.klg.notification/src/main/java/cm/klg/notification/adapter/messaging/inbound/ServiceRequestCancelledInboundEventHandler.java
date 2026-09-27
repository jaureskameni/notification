package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestCancelledEventDTO;

public record ServiceRequestCancelledInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceRequestServiceRequestCancelledEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceRequestDomainEventType.SERVICE_REQUEST_CANCELLED.getValue();
  }

  @Override
  public Class<ServiceRequestServiceRequestCancelledEventDTO> payloadType() {
    return ServiceRequestServiceRequestCancelledEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceRequestServiceRequestCancelledEventDTO> inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toServiceRequestCancelledNotificationCommand(
            inboxEventCommand.data()));
  }
}
