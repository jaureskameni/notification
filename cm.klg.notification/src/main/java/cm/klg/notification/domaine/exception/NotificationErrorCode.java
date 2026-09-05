package cm.klg.notification.domaine.exception;

import cm.klg.common.base.utils.ErrorCode;
import lombok.Getter;

public enum NotificationErrorCode implements ErrorCode {

  // ERROR-403
  NOTIFICATION_403_001("NOTIFICATION_403_001", "User Not Authorized To Open This Notification"),

  // ERROR-404,
  NOTIFICATION_404_001("NOTIFICATION_404_001", "User Not Found"),
  NOTIFICATION_404_003("NOTIFICATION_404_003", "Notification Not Found"),
  ;

  private final String value;
  @Getter private final String description;

  NotificationErrorCode(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String value() {
    return value;
  }
}
