package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestAcceptedEventDTO;

public record ServiceRequestAcceptedInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    UseCaseExecutor useCaseExecutor,
    MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<ServiceRequestServiceRequestAcceptedEventDTO> {
  @Override
  public String getEventType() {
    return ServiceRequestDomainEventType.SERVICE_REQUEST_ACCEPTED.getValue();
  }

  @Override
  public Class<ServiceRequestServiceRequestAcceptedEventDTO> getDataType() {
    return ServiceRequestServiceRequestAcceptedEventDTO.class;
  }

  @Override
  public void handle(
      ServiceRequestServiceRequestAcceptedEventDTO event, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            createNotificationUseCase.execute(
                messagingInboundMapper.toServiceRequestAcceptedNotificationCommand(event)));
  }
}
