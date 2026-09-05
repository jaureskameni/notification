package cm.klg.notification.domaine.user;

import org.jspecify.annotations.Nullable;

public record Email(String value) {
  public static @Nullable Email from(@Nullable String value) {
    if (value == null) {
      return null;
    }
    if (value.isBlank()) {
      throw new IllegalArgumentException("Email cannot be blank");
    }
    return new Email(value.trim());
  }
}
