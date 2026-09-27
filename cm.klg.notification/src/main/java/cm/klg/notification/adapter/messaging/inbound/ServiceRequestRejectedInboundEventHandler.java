package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestRejectedEventDTO;

public record ServiceRequestRejectedInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceRequestServiceRequestRejectedEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceRequestDomainEventType.SERVICE_REQUEST_REJECTED.getValue();
  }

  @Override
  public Class<ServiceRequestServiceRequestRejectedEventDTO> payloadType() {
    return ServiceRequestServiceRequestRejectedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceRequestServiceRequestRejectedEventDTO> inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toServiceRequestRejectedNotificationCommand(
            inboxEventCommand.data()));
  }
}
