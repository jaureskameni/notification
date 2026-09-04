package cm.klg.notification.domaine.notification;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.notification.domaine.user.UserId;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class Notification {
  private final NotificationId id;
  private final UserId recipientId;
  private final UserId actorId;
  private final NotificationType notificationType;
  private final ReferenceType referenceType;
  private final NotificationReferenceId referenceId;
  private final CreatedAt createdAt;
  private NotificationStatus status;
  @Nullable private CreatedAt readAt;
  @Nullable private CreatedAt openedAt;

  public Notification(
      NotificationId id,
      UserId recipientId,
      UserId actorId,
      NotificationType notificationType,
      ReferenceType referenceType,
      NotificationReferenceId referenceId,
      NotificationState state,
      CreatedAt createdAt) {
    this.id = id;
    this.recipientId = recipientId;
    this.actorId = actorId;
    this.notificationType = notificationType;
    this.referenceType = referenceType;
    this.referenceId = referenceId;
    this.status = state.status();
    this.createdAt = createdAt;
    this.readAt = state.readAt();
    this.openedAt = state.openedAt();
  }

  public static Notification of(
      UserId recipientId,
      UserId actorId,
      NotificationType notificationType,
      ReferenceType referenceType,
      NotificationReferenceId referenceId) {
    return new Notification(
        NotificationId.generate(),
        recipientId,
        actorId,
        notificationType,
        referenceType,
        referenceId,
        NotificationState.unread(),
        CreatedAt.from(LocalDateTime.now()));
  }

  public void markAsRead(CreatedAt readAt) {
    this.status = NotificationStatus.READ;
    this.readAt = readAt;
  }

  public void markAsOpened(UserId userId) {
    if (!Objects.equals(this.recipientId, userId)) {
      throw new UserNotAuthorizedToOpenNotificationException();
    }

    this.status = NotificationStatus.OPENED;

    if (this.openedAt == null) {
      this.openedAt = new CreatedAt(LocalDateTime.now());
    }

    if (this.readAt == null) {
      this.readAt = new CreatedAt(LocalDateTime.now());
    }
  }

  public static Notification reconstitute(
      NotificationId id,
      UserId recipientId,
      UserId actorId,
      NotificationType notificationType,
      ReferenceType referenceType,
      NotificationReferenceId referenceId,
      NotificationState state,
      CreatedAt createdAt) {
    return new Notification(
        id, recipientId, actorId, notificationType, referenceType, referenceId, state, createdAt);
  }
}
