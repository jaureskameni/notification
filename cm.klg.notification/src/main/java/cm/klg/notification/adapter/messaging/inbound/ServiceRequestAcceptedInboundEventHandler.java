package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestAcceptedEventDTO;

public record ServiceRequestAcceptedInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceRequestServiceRequestAcceptedEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceRequestDomainEventType.SERVICE_REQUEST_ACCEPTED.getValue();
  }

  @Override
  public Class<ServiceRequestServiceRequestAcceptedEventDTO> payloadType() {
    return ServiceRequestServiceRequestAcceptedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceRequestServiceRequestAcceptedEventDTO> inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toServiceRequestAcceptedNotificationCommand(
            inboxEventCommand.data()));
  }
}
