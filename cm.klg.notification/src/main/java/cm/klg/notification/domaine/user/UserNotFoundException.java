package cm.klg.notification.domaine.user;

import static cm.klg.notification.domaine.exception.NotificationErrorCode.NOTIFICATION_404_001;

import cm.klg.common.base.exception.DomainException;

public class UserNotFoundException extends DomainException {
  public UserNotFoundException() {
    super(NOTIFICATION_404_001);
  }
}
