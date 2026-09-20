package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

public record RejectServiceProviderInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<ServiceProviderServiceProviderRejectedEventDTO> {
  @Override
  public String getEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_REJECTED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderRejectedEventDTO> getDataType() {
    return ServiceProviderServiceProviderRejectedEventDTO.class;
  }

  @Override
  public void handle(
      ServiceProviderServiceProviderRejectedEventDTO providerRejectedEventDTO,
      InboxEventCommand inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toRejectedNotificationCommand(providerRejectedEventDTO));
  }
}
