package cm.klg.notification.domaine.notification;

import static cm.klg.notification.domaine.exception.NotificationErrorCode.NOTIFICATION_404_003;

import cm.klg.common.base.exception.DomainException;

public class UserNotAuthorizedToOpenNotificationException extends DomainException {
  public UserNotAuthorizedToOpenNotificationException() {
    super(NOTIFICATION_404_003);
  }
}
