package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import cm.klg.common.base.transaction.UseCaseExecutor;
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
  @Mock private UseCaseExecutor useCaseExecutor;

  @InjectMocks private DeleteUserInboundEventHandler handler;

  @Test
  void shouldExposeUserDeletedEventTypeTest() {
    assertThat(handler.getEventType()).isEqualTo(UamDomainEventType.USER_DELETED.getValue());
  }

  @Test
  void shouldExposeUamUserDeletedEventDataTypeTest() {
    assertThat(handler.getDataType()).isEqualTo(UamUserDeletedEventDTO.class);
  }

  @Test
  void shouldNotExecuteCommandWhenExecutorDoesNotRunItTest() {
    UamUserDeletedEventDTO event = givenUserDeletedEvent();
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    handler.handle(event, inboxEventCommand);

    verify(deleteUserUseCase, never()).execute(any(UserId.class));
    verify(useCaseExecutor).runCommand(any(Runnable.class));
  }

  @Test
  void shouldPropagateExceptionRaisedByUseCaseTest() {
    UamUserDeletedEventDTO event = givenUserDeletedEvent();
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    doThrow(new IllegalStateException("boom")).when(deleteUserUseCase).execute(any(UserId.class));
    runCommandWhenExecuted(useCaseExecutor);

    assertThatThrownBy(() -> handler.handle(event, inboxEventCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("boom");
  }

  @Test
  void shouldDeleteUserFromEventDataThroughFullFlowTest() {
    UUID userId = UUID.randomUUID();
    UamUserDeletedEventDTO event = new UamUserDeletedEventDTO().userId(userId);
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    UserRepository userRepository = mock(UserRepository.class);
    DeleteUserInboundEventHandler realFlowHandler =
        new DeleteUserInboundEventHandler(new DeleteUserUseCase(userRepository), useCaseExecutor);
    runCommandWhenExecuted(useCaseExecutor);

    realFlowHandler.handle(event, inboxEventCommand);

    verify(userRepository).deleteById(UserId.from(userId));
    verify(useCaseExecutor).runCommand(any(Runnable.class));
  }

  private static UamUserDeletedEventDTO givenUserDeletedEvent() {
    return new UamUserDeletedEventDTO().userId(UUID.randomUUID());
  }

  private static InboxEventCommand givenInboxEventCommand(UamUserDeletedEventDTO event) {
    return new InboxEventCommand(
        UUID.randomUUID(),
        "user",
        UamDomainEventType.USER_DELETED.getValue(),
        String.valueOf(event.getUserId()),
        event);
  }

  private static void runCommandWhenExecuted(UseCaseExecutor executor) {
    doAnswer(
            invocation -> {
              invocation.<Runnable>getArgument(0).run();
              return null;
            })
        .when(executor)
        .runCommand(any(Runnable.class));
  }
}
