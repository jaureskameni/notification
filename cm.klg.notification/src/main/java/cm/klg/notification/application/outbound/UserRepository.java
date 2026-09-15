package cm.klg.notification.application.outbound;

import cm.klg.notification.domaine.user.User;
import cm.klg.notification.domaine.user.UserId;

public interface UserRepository {
  void insertIfAbsent(User newUser);

  void update(User user);

  void deleteById(UserId userId);
}
