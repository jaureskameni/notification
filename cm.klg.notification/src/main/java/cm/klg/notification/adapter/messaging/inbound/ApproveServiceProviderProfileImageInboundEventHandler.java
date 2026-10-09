package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderProfileImageApprovedEventDTO;

public record ApproveServiceProviderProfileImageInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    MessagingInboundMapper messagingInboundMapper)
    implements InboundEventHandler<ServiceProviderServiceProviderProfileImageApprovedEventDTO> {
  @Override
  public String handledEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_PROFILE_IMAGE_APPROVED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderProfileImageApprovedEventDTO> payloadType() {
    return ServiceProviderServiceProviderProfileImageApprovedEventDTO.class;
  }

  @Override
  public void handle(
      InboxEventCommand<ServiceProviderServiceProviderProfileImageApprovedEventDTO>
          inboxEventCommand) {
    createNotificationUseCase.execute(
        messagingInboundMapper.toProfileImageApprovedNotificationCommand(inboxEventCommand.data()));
  }
}
