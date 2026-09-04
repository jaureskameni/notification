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
import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.application.usecase.CreateNewUserUseCase;
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
class CreateUserInboundEventHandlerTest {

  @Mock private CreateNewUserUseCase createNewUserUseCase;
  @Mock private MessagingInboundMapper messagingInboundMapper;
  @Mock private UseCaseExecutor useCaseExecutor;

  @InjectMocks private CreateUserInboundEventHandler handler;

  @Test
  void shouldExposeUserCreatedEventTypeTest() {
    assertThat(handler.getEventType()).isEqualTo(UamDomainEventType.USER_CREATED.getValue());
  }

  @Test
  void shouldExposeUamUserCreatedEventDataTypeTest() {
    assertThat(handler.getDataType()).isEqualTo(UamUserCreatedEventDTO.class);
  }

  @Test
  void shouldNotExecuteCommandWhenExecutorDoesNotRunItTest() {
    UamUserCreatedEventDTO event = givenUserCreatedEvent();
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    handler.handle(event, inboxEventCommand);

    verify(messagingInboundMapper, never()).toCreateUserCommand(event);
    verify(createNewUserUseCase, never())
        .execute(any(CreateNewUserUseCase.CreateNewUserCommand.class));
    verify(useCaseExecutor).runCommand(any(Runnable.class));
  }

  @Test
  void shouldPropagateExceptionRaisedByUseCaseTest() {
    UamUserCreatedEventDTO event = givenUserCreatedEvent();
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);
    CreateNewUserUseCase.CreateNewUserCommand command = givenCreateUserCommand();

    when(messagingInboundMapper.toCreateUserCommand(event)).thenReturn(command);
    doThrow(new IllegalStateException("boom")).when(createNewUserUseCase).execute(command);
    runCommandWhenExecuted(useCaseExecutor);

    assertThatThrownBy(() -> handler.handle(event, inboxEventCommand))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("boom");
  }

  @Test
  void shouldCreateUserFromEventDataThroughFullFlowTest() {
    UUID userId = UUID.randomUUID();
    UUID identityId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now().minusMinutes(10);
    UamUserCreatedEventDTO event =
        new UamUserCreatedEventDTO()
            .id(userId)
            .identityId(identityId)
            .lastname("Doe")
            .firstname(" John ")
            .email("john.doe@example.com")
            .phoneNumber(new UamPhoneNumberDTO().countryCode("+237").number("699999999"))
            .createdAt(createdAt);
    InboxEventCommand inboxEventCommand = givenInboxEventCommand(event);

    UserRepository userRepository = mock(UserRepository.class);
    CreateUserInboundEventHandler realFlowHandler =
        new CreateUserInboundEventHandler(
            new CreateNewUserUseCase(userRepository),
            new MessagingInboundMapperImpl(),
            useCaseExecutor);
    runCommandWhenExecuted(useCaseExecutor);

    realFlowHandler.handle(event, inboxEventCommand);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).insertIfAbsent(userCaptor.capture());

    assertThat(userCaptor.getValue())
        .satisfies(
            user -> {
              assertThat(user.getId().value()).isEqualTo(userId);
              assertThat(user.getIdentityId().value()).isEqualTo(identityId);
              assertThat(user.getLastname().value()).isEqualTo("Doe");
              assertThat(user.getFirstname()).isNotNull();
              assertThat(user.getFirstname().value()).isEqualTo("John");
              assertThat(user.getEmail()).isNotNull();
              assertThat(user.getEmail().value()).isEqualTo("john.doe@example.com");
              assertThat(user.getPhoneNumber().countryCode()).isEqualTo("+237");
              assertThat(user.getPhoneNumber().number()).isEqualTo("699999999");
              assertThat(user.getCreatedAt().value()).isEqualTo(createdAt);
            });
    verify(useCaseExecutor).runCommand(any(Runnable.class));
  }

  private static UamUserCreatedEventDTO givenUserCreatedEvent() {
    return new UamUserCreatedEventDTO()
        .id(UUID.randomUUID())
        .identityId(UUID.randomUUID())
        .lastname("Doe")
        .firstname(" John ")
        .email("john.doe@example.com")
        .phoneNumber(new UamPhoneNumberDTO().countryCode("+237").number("699999999"))
        .createdAt(LocalDateTime.now().minusMinutes(10));
  }

  private static InboxEventCommand givenInboxEventCommand(UamUserCreatedEventDTO event) {
    return new InboxEventCommand(
        UUID.randomUUID(),
        "user",
        UamDomainEventType.USER_CREATED.getValue(),
        String.valueOf(event.getId()),
        event);
  }

  private static CreateNewUserUseCase.CreateNewUserCommand givenCreateUserCommand() {
    return new CreateNewUserUseCase.CreateNewUserCommand(
        UUID.randomUUID(),
        UUID.randomUUID(),
        new CreateNewUserUseCase.CreateNewUserCommand.UserProfileCommand(
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
