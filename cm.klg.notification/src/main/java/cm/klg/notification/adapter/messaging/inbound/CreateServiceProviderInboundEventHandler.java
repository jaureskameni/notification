package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;

public record CreateServiceProviderInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceProviderServiceProviderCreatedEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_CREATED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderCreatedEventDTO> payloadType() {
    return ServiceProviderServiceProviderCreatedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceProviderServiceProviderCreatedEventDTO> inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toCreatedNotificationCommand(inboxEventCommand.data()));
  }
}
