package cm.klg.notification.domaine.notification;

import cm.klg.common.base.domain.CreatedAt;
import org.jspecify.annotations.Nullable;

public record NotificationState(
    NotificationStatus status, @Nullable CreatedAt readAt, @Nullable CreatedAt openedAt) {
  public static NotificationState unread() {
    return new NotificationState(NotificationStatus.UNREAD, null, null);
  }
}
