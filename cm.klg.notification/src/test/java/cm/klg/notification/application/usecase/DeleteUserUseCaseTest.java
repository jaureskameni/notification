package cm.klg.notification.application.usecase;

import static org.mockito.Mockito.verify;

import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.domaine.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteUserUseCaseTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private DeleteUserUseCase deleteUserUseCase;

  @Test
  void shouldDeleteUserByIdTest() {
    UserId userId = UserId.from(UUID.randomUUID());

    deleteUserUseCase.execute(userId);

    verify(userRepository).deleteById(userId);
  }
}
