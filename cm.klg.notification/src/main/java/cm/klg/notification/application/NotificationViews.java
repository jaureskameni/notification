package cm.klg.notification.application;

import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface NotificationViews {
  UUID getId();

  UUID getRecipientId();

  UUID getActorId();

  UUID getReferenceId();

  String getNotificationType();

  String getReferenceType();

  String getStatus();

  LocalDateTime getCreatedAt();

  @Nullable LocalDateTime getReadAt();

  @Nullable LocalDateTime getOpenedAt();
}
