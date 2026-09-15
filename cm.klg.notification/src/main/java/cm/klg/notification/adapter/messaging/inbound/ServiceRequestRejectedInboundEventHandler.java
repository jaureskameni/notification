package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestRejectedEventDTO;

public record ServiceRequestRejectedInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    UseCaseExecutor useCaseExecutor,
    MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<ServiceRequestServiceRequestRejectedEventDTO> {
  @Override
  public String getEventType() {
    return ServiceRequestDomainEventType.SERVICE_REQUEST_REJECTED.getValue();
  }

  @Override
  public Class<ServiceRequestServiceRequestRejectedEventDTO> getDataType() {
    return ServiceRequestServiceRequestRejectedEventDTO.class;
  }

  @Override
  public void handle(
      ServiceRequestServiceRequestRejectedEventDTO event, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            createNotificationUseCase.execute(
                messagingInboundMapper.toServiceRequestRejectedNotificationCommand(event)));
  }
}
