package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceRequestDomainEventType;
import org.openapitools.model.ServiceRequestServiceRequestCreatedEventDTO;

public record ServiceRequestCreatedInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    UseCaseExecutor useCaseExecutor,
    MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<ServiceRequestServiceRequestCreatedEventDTO> {
  @Override
  public String getEventType() {
    return ServiceRequestDomainEventType.SERVICE_REQUEST_CREATED.getValue();
  }

  @Override
  public Class<ServiceRequestServiceRequestCreatedEventDTO> getDataType() {
    return ServiceRequestServiceRequestCreatedEventDTO.class;
  }

  @Override
  public void handle(
      ServiceRequestServiceRequestCreatedEventDTO event, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            createNotificationUseCase.execute(
                messagingInboundMapper.toServiceRequestCreatedNotificationCommand(event)));
  }
}
