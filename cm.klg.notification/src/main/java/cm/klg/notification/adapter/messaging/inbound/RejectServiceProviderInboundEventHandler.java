package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

public record RejectServiceProviderInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceProviderServiceProviderRejectedEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_REJECTED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderRejectedEventDTO> payloadType() {
    return ServiceProviderServiceProviderRejectedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceProviderServiceProviderRejectedEventDTO> inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toRejectedNotificationCommand(inboxEventCommand.data()));
  }
}
