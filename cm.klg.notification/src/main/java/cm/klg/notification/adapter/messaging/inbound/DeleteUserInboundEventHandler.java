package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserDeletedEventDTO;
import cm.klg.notification.application.usecase.DeleteUserUseCase;
import cm.klg.notification.domaine.user.UserId;
import com.emb.application.handler.InboundEventHandler;
import com.emb.domain.inboxevent.InboxEventCommand;

public record DeleteUserInboundEventHandler(DeleteUserUseCase deleteUserUseCase)
    implements InboundEventHandler<UamUserDeletedEventDTO> {
  @Override
  public String handledEventType() {
    return UamDomainEventType.USER_DELETED.getValue();
  }

  @Override
  public Class<UamUserDeletedEventDTO> payloadType() {
    return UamUserDeletedEventDTO.class;
  }

  @Override
  public void handle(InboxEventCommand<UamUserDeletedEventDTO> inboxEventCommand) {
    deleteUserUseCase.execute(UserId.from(inboxEventCommand.data().getId()));
  }
}
