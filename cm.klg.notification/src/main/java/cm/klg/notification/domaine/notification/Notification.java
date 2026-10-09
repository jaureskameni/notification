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
  private final RejectionReason reason;
  private final CreatedAt createdAt;
  private NotificationStatus status;
  @Nullable private CreatedAt readAt;
  @Nullable private CreatedAt openedAt;

  public Notification(
      NotificationId id,
      NotificationDetails details,
      NotificationState state,
      CreatedAt createdAt) {
    this.id = id;
    this.recipientId = details.recipientId();
    this.actorId = details.actorId();
    this.notificationType = details.notificationType();
    this.referenceType = details.referenceType();
    this.referenceId = details.referenceId();
    this.reason = details.reason();
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
    return of(
        recipientId, actorId, notificationType, referenceType, referenceId, RejectionReason.NULL);
  }

  public static Notification of(
      UserId recipientId,
      UserId actorId,
      NotificationType notificationType,
      ReferenceType referenceType,
      NotificationReferenceId referenceId,
      RejectionReason reason) {
    return new Notification(
        NotificationId.generate(),
        new NotificationDetails(
            recipientId, actorId, notificationType, referenceType, referenceId, reason),
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
      NotificationDetails details,
      NotificationState state,
      CreatedAt createdAt) {
    return new Notification(id, details, state, createdAt);
  }
}
