package cm.klg.notification.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

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
class UpdateUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UpdateUserUseCase updateUserUseCase;

  @Test
  void shouldUpdateUserTest() {
    UUID userId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now().minusMinutes(5);
    UpdateUserUseCase.UpdateUserCommand command =
        new UpdateUserUseCase.UpdateUserCommand(
            userId,
            new UpdateUserUseCase.UpdateUserCommand.UserProfileCommand(
                "Doe", " John ", "john.doe@example.com"),
            "+237",
            "699999999",
            now);

    updateUserUseCase.execute(command);

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
              assertThat(user.getCreatedAt().value()).isEqualTo(now);
            });
  }

  @Test
  void shouldUpdateUserWithNullableFieldsTest() {
    UUID userId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now().minusMinutes(5);
    UpdateUserUseCase.UpdateUserCommand command =
        new UpdateUserUseCase.UpdateUserCommand(
            userId,
            new UpdateUserUseCase.UpdateUserCommand.UserProfileCommand("Doe", null, null),
            "+237",
            "699999999",
            now);

    updateUserUseCase.execute(command);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).update(userCaptor.capture());

    assertThat(userCaptor.getValue().getFirstname()).isNull();
    assertThat(userCaptor.getValue().getEmail()).isNull();
    assertThat(userCaptor.getValue().getLastname().value()).isEqualTo("Doe");
    assertThat(userCaptor.getValue().getPhoneNumber().value()).isEqualTo("+237699999999");
  }
}
