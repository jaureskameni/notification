package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserDeletedEventDTO;
import cm.klg.notification.application.usecase.DeleteUserUseCase;
import cm.klg.notification.domaine.user.UserId;
import com.emb.application.handler.InboxEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;

public record DeleteUserInboundEventHandler(
    DeleteUserUseCase deleteUserUseCase, UseCaseExecutor useCaseExecutor)
    implements InboxEventHandler<UamUserDeletedEventDTO> {
  @Override
  public String getEventType() {
    return UamDomainEventType.USER_DELETED.getValue();
  }

  @Override
  public Class<UamUserDeletedEventDTO> getDataType() {
    return UamUserDeletedEventDTO.class;
  }

  @Override
  public void handle(
      UamUserDeletedEventDTO userDeletedEventDTO, InboxEventCommand inboxEventCommand) {
    useCaseExecutor.runCommand(
        () -> deleteUserUseCase.execute(UserId.from(userDeletedEventDTO.getUserId())));
  }
}
