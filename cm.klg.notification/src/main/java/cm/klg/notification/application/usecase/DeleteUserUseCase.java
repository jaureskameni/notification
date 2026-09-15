package cm.klg.notification.application.usecase;

import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.domaine.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserUseCase {
  private final UserRepository userRepository;

  public void execute(UserId userId) {
    userRepository.deleteById(userId);
  }
}
