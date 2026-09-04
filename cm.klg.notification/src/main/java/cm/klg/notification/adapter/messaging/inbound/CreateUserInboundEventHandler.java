package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.notification.application.usecase.CreateNewUserUseCase;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;

public record CreateUserInboundEventHandler(
    CreateNewUserUseCase createNewUserUseCase,
    MessagingInboundMapper messagingInboundMapper,
    UseCaseExecutor useCaseExecutor)
    implements InboxEventHandler<UamUserCreatedEventDTO> {
  @Override
  public String getEventType() {
    return UamDomainEventType.USER_CREATED.getValue();
  }

  @Override
  public Class<UamUserCreatedEventDTO> getDataType() {
    return UamUserCreatedEventDTO.class;
  }

  @Override
  public void handle(
      UamUserCreatedEventDTO userCreatedEventDTO, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () ->
            createNewUserUseCase.execute(
                messagingInboundMapper.toCreateUserCommand(userCreatedEventDTO)));
  }
}
