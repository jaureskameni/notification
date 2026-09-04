package cm.klg.notification.domaine.notification;

import java.util.UUID;

public record NotificationId(UUID value) {
  public static NotificationId generate() {
    return new NotificationId(UUID.randomUUID());
  }

  public static NotificationId from(UUID value) {
    return new NotificationId(value);
  }
}
