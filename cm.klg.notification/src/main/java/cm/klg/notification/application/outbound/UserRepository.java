package cm.klg.notification.application.outbound;

import cm.klg.notification.domaine.user.User;

public interface UserRepository {
  void insertIfAbsent(User newUser);
}
