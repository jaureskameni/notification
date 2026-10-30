package cm.klg.notification.application;

import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface UserView {
  UUID getId();

  @Nullable String getFirstname();

  String getLastname();

  LocalDateTime getCreatedAt();
}
