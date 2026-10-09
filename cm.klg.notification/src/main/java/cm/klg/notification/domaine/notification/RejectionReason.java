package cm.klg.notification.domaine.notification;

import org.jspecify.annotations.Nullable;

public record RejectionReason(String value) {

  public static final RejectionReason NULL = new RejectionReason("");

  public static RejectionReason from(@Nullable String value) {
    return value == null ? NULL : new RejectionReason(value);
  }
}
