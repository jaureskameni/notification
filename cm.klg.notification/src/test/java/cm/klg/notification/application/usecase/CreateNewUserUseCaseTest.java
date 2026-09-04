package cm.klg.notification.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.domaine.user.User;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateNewUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private CreateNewUserUseCase createNewUserUseCase;

  @Test
  void shouldCreateNewUserTest() {
    UUID userId = UUID.randomUUID();
    UUID identityId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now().minusMinutes(5);
    CreateNewUserUseCase.CreateNewUserCommand command =
        new CreateNewUserUseCase.CreateNewUserCommand(
            userId,
            identityId,
            new CreateNewUserUseCase.CreateNewUserCommand.UserProfileCommand(
                "Doe", " John ", "john.doe@example.com"),
            "+237",
            "699999999",
            now);

    createNewUserUseCase.execute(command);

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
              assertThat(user.getCreatedAt().value()).isEqualTo(now);
            });
  }

  @Test
  void shouldCreateUserWithNullableFirstnameTest() {
    UUID userId = UUID.randomUUID();
    UUID identityId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now().minusMinutes(5);
    CreateNewUserUseCase.CreateNewUserCommand command =
        new CreateNewUserUseCase.CreateNewUserCommand(
            userId,
            identityId,
            new CreateNewUserUseCase.CreateNewUserCommand.UserProfileCommand("Doe", null, null),
            "+237",
            "699999999",
            now);

    createNewUserUseCase.execute(command);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).insertIfAbsent(userCaptor.capture());

    assertThat(userCaptor.getValue().getFirstname()).isNull();
    assertThat(userCaptor.getValue().getEmail()).isNull();
    assertThat(userCaptor.getValue().getLastname().value()).isEqualTo("Doe");
    assertThat(userCaptor.getValue().getPhoneNumber().value()).isEqualTo("+237699999999");
  }

  @Test
  void shouldRejectBlankFirstnameTest() {
    CreateNewUserUseCase.CreateNewUserCommand command =
        new CreateNewUserUseCase.CreateNewUserCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            new CreateNewUserUseCase.CreateNewUserCommand.UserProfileCommand(
                "Doe", " ", "john.doe@example.com"),
            "+237",
            "699999999",
            LocalDateTime.now().minusMinutes(5));

    assertThatThrownBy(() -> createNewUserUseCase.execute(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Firstname cannot be blank");
    verifyNoInteractions(userRepository);
  }

  @Test
  void shouldRejectFutureCreatedAtTest() {
    CreateNewUserUseCase.CreateNewUserCommand command =
        new CreateNewUserUseCase.CreateNewUserCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            new CreateNewUserUseCase.CreateNewUserCommand.UserProfileCommand(
                "Doe", "John", "john.doe@example.com"),
            "+237",
            "699999999",
            LocalDateTime.now().plusMinutes(5));

    assertThatThrownBy(() -> createNewUserUseCase.execute(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("The given timestamp is already in the future");
    verifyNoInteractions(userRepository);
  }
}
