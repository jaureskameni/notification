package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserDeletedEventDTO;
import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.application.usecase.DeleteUserUseCase;
import cm.klg.notification.domaine.user.UserId;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserInboundEventHandlerTest {

  @Mock private DeleteUserUseCase deleteUserUseCase;

  @InjectMocks private DeleteUserInboundEventHandler handler;

  @Test
  void shouldExposeUserDeletedEventTypeTest() {
    assertThat(handler.handledEventType()).isEqualTo(UamDomainEventType.USER_DELETED.getValue());
  }

  @Test
  void shouldExposeUamUserDeletedEventDataTypeTest() {
    assertThat(handler.payloadType()).isEqualTo(UamUserDeletedEventDTO.class);
  }

  @Test
  void shouldDeleteUserFromEventDataTest() {
    UUID userId = UUID.randomUUID();
    UamUserDeletedEventDTO event = new UamUserDeletedEventDTO().id(userId);
    InboxEventCommand<UamUserDeletedEventDTO> inboxEventCommand = givenInboxEventCommand(event);

    handler.handle(inboxEventCommand);

    verify(deleteUserUseCase).execute(UserId.from(userId));
  }

  @Test
  void shouldPropagateExceptionRaisedByUseCaseTest() {
    UamUserDeletedEventDTO event = givenUserDeletedEvent();
    InboxEventCommand<UamUserDeletedEventDTO> inboxEventCommand = givenInboxEventCommand(event);

    doThrow(new IllegalStateException("boom")).when(deleteUserUseCase).execute(any(UserId.class));

    assertThatThrownBy(() -> handler.handle(inboxEventCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("boom");
  }

  @Test
  void shouldDeleteUserFromEventDataThroughFullFlowTest() {
    UUID userId = UUID.randomUUID();
    UamUserDeletedEventDTO event = new UamUserDeletedEventDTO().id(userId);
    InboxEventCommand<UamUserDeletedEventDTO> inboxEventCommand = givenInboxEventCommand(event);

    UserRepository userRepository = mock(UserRepository.class);
    DeleteUserInboundEventHandler realFlowHandler =
        new DeleteUserInboundEventHandler(new DeleteUserUseCase(userRepository));

    realFlowHandler.handle(inboxEventCommand);

    verify(userRepository).deleteById(UserId.from(userId));
  }

  private static UamUserDeletedEventDTO givenUserDeletedEvent() {
    return new UamUserDeletedEventDTO().id(UUID.randomUUID());
  }

  private static InboxEventCommand<UamUserDeletedEventDTO> givenInboxEventCommand(
      UamUserDeletedEventDTO event) {
    return new InboxEventCommand<>(
        UUID.randomUUID(),
        "user",
        UamDomainEventType.USER_DELETED.getValue(),
        String.valueOf(event.getId()),
        event);
  }
}
