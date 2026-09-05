package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;
import org.openapitools.model.ServiceProviderDomainEventType;
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;

public record CreateServiceProviderInboundEventHandler(
    CreateNotificationUseCase createNotificationUseCase,
    UseCaseExecutor useCaseExecutor,
    MessagingInboundMapper messagingInboundMapper)
    implements InboxEventHandler<ServiceProviderServiceProviderCreatedEventDTO> {
  @Override
  public String getEventType() {
    return ServiceProviderDomainEventType.SERVICE_PROVIDER_CREATED.getValue();
  }

  @Override
  public Class<ServiceProviderServiceProviderCreatedEventDTO> getDataType() {
    return ServiceProviderServiceProviderCreatedEventDTO.class;
  }

  @Override
  public void handle(
      ServiceProviderServiceProviderCreatedEventDTO providerCreatedEventDTO,
      InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            createNotificationUseCase.execute(
                messagingInboundMapper.toCreatedNotificationCommand(providerCreatedEventDTO)));
  }
}
