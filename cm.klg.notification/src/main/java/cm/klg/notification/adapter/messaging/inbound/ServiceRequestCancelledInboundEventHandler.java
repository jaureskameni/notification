package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestCancelledEventDTO;

public record ServiceRequestCancelledInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    UseCaseExecutor useCaseExecutor,
    MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<ServiceRequestServiceRequestCancelledEventDTO> {
  @Override
  public String getEventType() {
    return ServiceRequestDomainEventType.SERVICE_REQUEST_CANCELLED.getValue();
  }

  @Override
  public Class<ServiceRequestServiceRequestCancelledEventDTO> getDataType() {
    return ServiceRequestServiceRequestCancelledEventDTO.class;
  }

  @Override
  public void handle(
      ServiceRequestServiceRequestCancelledEventDTO event, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            createNotificationUseCase.execute(
                messagingInboundMapper.toServiceRequestCancelledNotificationCommand(event)));
  }
}
