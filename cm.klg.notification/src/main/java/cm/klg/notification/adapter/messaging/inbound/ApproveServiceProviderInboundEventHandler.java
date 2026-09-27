package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;

public record ApproveServiceProviderInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceProviderServiceProviderApprovedEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_APPROVED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderApprovedEventDTO> payloadType() {
    return ServiceProviderServiceProviderApprovedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceProviderServiceProviderApprovedEventDTO> inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toApprovedNotificationCommand(inboxEventCommand.data()));
  }
}
