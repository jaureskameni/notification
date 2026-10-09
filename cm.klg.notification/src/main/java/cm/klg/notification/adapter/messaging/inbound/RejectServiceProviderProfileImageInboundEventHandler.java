package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderProfileImageRejectedEventDTO;

public record RejectServiceProviderProfileImageInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceProviderServiceProviderProfileImageRejectedEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_PROFILE_IMAGE_REJECTED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderProfileImageRejectedEventDTO> payloadType() {
    return ServiceProviderServiceProviderProfileImageRejectedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceProviderServiceProviderProfileImageRejectedEventDTO>
          inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toProfileImageRejectedNotificationCommand(inboxEventCommand.data()));
  }
}
