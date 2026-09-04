package cm.klg.notification.domaine.notification;

import java.util.UUID;

public record NotificationReferenceId(UUID value) {
  public static NotificationReferenceId from(UUID value) {
    return new NotificationReferenceId(value);
  }
}
