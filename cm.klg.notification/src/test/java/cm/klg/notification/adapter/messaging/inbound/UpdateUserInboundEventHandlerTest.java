package cm.klg.notification.adapter.messaging.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamDomainEventType;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamPhoneNumberDTO;
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserUpdatedEventDTO;
import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.application.usecase.UpdateUserUseCase;
import cm.klg.notification.domaine.user.User;
import com.emb.domain.inboxevent.InboxEventCommand;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateUserInboundEventHandlerTest {

  @Mock private UpdateUserUseCase updateUserUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;
  @Mock private UseCaseExecutor useCaseExecutor;

  @InjectMocks private UpdateUserInboundEventHandler handler;

  @Test
  void shouldExposeUserUpdatedEventTypeTest() {
    assertThat(handler.getEventType()).isEqualTo(UamDomainEventType.USER_UPDATED.getValue());
  }

  @Test
  void shouldExposeUamUserUpdatedEventDataTypeTest() {
    assertThat(handler.getDataType()).isEqualTo(UamUserUpdatedEventDTO.class);
  }

  @Test
  void shouldNotExecuteCommandWhenExecutorDoesNotRunItTest() {
    UamUserUpdatedEventDTO event = givenUserUpdatedEvent();
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    handler.handle(event, inboxEventCommand);

    verify(messagingInboundMapper, never()).toUpdateUserCommand(event);
    verify(updateUserUseCase, never()).execute(any(UpdateUserUseCase.UpdateUserCommand.class));
    verify(useCaseExecutor).runCommand(any(Runnable.class));
  }

  @Test
  void shouldPropagateExceptionRaisedByUseCaseTest() {
    UamUserUpdatedEventDTO event = givenUserUpdatedEvent();
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);
    UpdateUserUseCase.UpdateUserCommand command = givenUpdateUserCommand();

    when(messagingInboundMapper.toUpdateUserCommand(event)).thenReturn(command);
    doThrow(new IllegalStateException("boom")).when(updateUserUseCase).execute(command);
    runCommandWhenExecuted(useCaseExecutor);

    assertThatThrownBy(() -> handler.handle(event, inboxEventCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("boom");
  }

  @Test
  void shouldUpdateUserFromEventDataThroughFullFlowTest() {
    UUID userId = UUID.randomUUID();
    LocalDateTime updatedAt = LocalDateTime.now().minusMinutes(10);
    UamUserUpdatedEventDTO event =
        new UamUserUpdatedEventDTO()
            .userId(userId)
            .lastname("Doe")
            .firstname(" John ")
            .email("john.doe@example.com")
            .phoneNumber(new UamPhoneNumberDTO().countryCode("+237").number("699999999"))
            .updatedAt(updatedAt);
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    UserRepository userRepository = mock(UserRepository.class);
    UpdateUserInboundEventHandler realFlowHandler =
        new UpdateUserInboundEventHandler(
            new UpdateUserUseCase(userRepository),
            new MessagingInboundMapperImpl(),
            useCaseExecutor);
    runCommandWhenExecuted(useCaseExecutor);

    realFlowHandler.handle(event, inboxEventCommand);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).update(userCaptor.capture());

    assertThat(userCaptor.getValue())
        .satisfies(
            user -> {
              assertThat(user.getId().value()).isEqualTo(userId);
              assertThat(user.getLastname().value()).isEqualTo("Doe");
              assertThat(user.getFirstname()).isNotNull();
              assertThat(user.getFirstname().value()).isEqualTo("John");
              assertThat(user.getEmail()).isNotNull();
              assertThat(user.getEmail().value()).isEqualTo("john.doe@example.com");
              assertThat(user.getPhoneNumber().countryCode()).isEqualTo("+237");
              assertThat(user.getPhoneNumber().number()).isEqualTo("699999999");
              assertThat(user.getCreatedAt().value()).isEqualTo(updatedAt);
            });
    verify(useCaseExecutor).runCommand(any(Runnable.class));
  }

  private static UamUserUpdatedEventDTO givenUserUpdatedEvent() {
    return new UamUserUpdatedEventDTO()
        .userId(UUID.randomUUID())
        .lastname("Doe")
        .firstname(" John ")
        .email("john.doe@example.com")
        .phoneNumber(new UamPhoneNumberDTO().countryCode("+237").number("699999999"))
        .updatedAt(LocalDateTime.now().minusMinutes(10));
  }

  private static InboxEventCommand givenInboxEventCommand(UamUserUpdatedEventDTO event) {
    return new InboxEventCommand(
        UUID.randomUUID(),
        "user",
        UamDomainEventType.USER_UPDATED.getValue(),
        String.valueOf(event.getUserId()),
        event);
  }

  private static UpdateUserUseCase.UpdateUserCommand givenUpdateUserCommand() {
    return new UpdateUserUseCase.UpdateUserCommand(
        UUID.randomUUID(),
        new UpdateUserUseCase.UpdateUserCommand.UserProfileCommand(
            "Doe", " John ", "john.doe@example.com"),
        "+237",
        "699999999",
        LocalDateTime.now().minusDays(1));
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
