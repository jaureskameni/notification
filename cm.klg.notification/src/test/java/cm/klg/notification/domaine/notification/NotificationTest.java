package cm.klg.notification.domaine.notification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.notification.domaine.user.UserId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class NotificationTest {

  @Test
  void shouldCreateNotificationWithUnreadStatusTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    UserId actorId = UserId.from(UUID.randomUUID());

    Notification notification =
        Notification.of(
            recipientId,
            actorId,
            NotificationType.SERVICE_PROVIDER_CREATED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()));

    assertThat(notification.getId()).isNotNull();
    assertThat(notification.getRecipientId()).isEqualTo(recipientId);
    assertThat(notification.getActorId()).isEqualTo(actorId);
    assertThat(notification.getNotificationType())
        .isEqualTo(NotificationType.SERVICE_PROVIDER_CREATED);
    assertThat(notification.getReferenceType()).isEqualTo(ReferenceType.SERVICE_PROVIDER);
    assertThat(notification.getStatus()).isEqualTo(NotificationStatus.UNREAD);
    assertThat(notification.getReadAt()).isNull();
    assertThat(notification.getOpenedAt()).isNull();
  }

  @Test
  void shouldMarkNotificationAsReadTest() {
    Notification notification =
        Notification.of(
            UserId.from(UUID.randomUUID()),
            UserId.from(UUID.randomUUID()),
            NotificationType.SERVICE_PROVIDER_CREATED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()));

    CreatedAt readAt = CreatedAt.from(LocalDateTime.now());
    notification.markAsRead(readAt);

    assertThat(notification.getStatus()).isEqualTo(NotificationStatus.READ);
    assertThat(notification.getReadAt()).isEqualTo(readAt);
  }

  @Test
  void shouldMarkNotificationAsOpenedTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    Notification notification =
        Notification.of(
            recipientId,
            UserId.from(UUID.randomUUID()),
            NotificationType.SERVICE_PROVIDER_CREATED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()));

    notification.markAsOpened(recipientId);

    assertThat(notification.getStatus()).isEqualTo(NotificationStatus.OPENED);
    assertThat(notification.getOpenedAt()).isNotNull();
    assertThat(notification.getReadAt()).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenUnauthorizedUserTriesToOpenNotificationTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    Notification notification =
        Notification.of(
            recipientId,
            UserId.from(UUID.randomUUID()),
            NotificationType.SERVICE_PROVIDER_CREATED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()));

    UserId unauthorizedUser = UserId.from(UUID.randomUUID());

    assertThatThrownBy(() -> notification.markAsOpened(unauthorizedUser))
        .isInstanceOf(UserNotAuthorizedToOpenNotificationException.class);
  }

  @Test
  void shouldPreserveExistingOpenedAtWhenMarkingAsOpenedAgainTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    Notification notification =
        Notification.of(
            recipientId,
            UserId.from(UUID.randomUUID()),
            NotificationType.SERVICE_PROVIDER_CREATED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()));

    notification.markAsOpened(recipientId);
    CreatedAt firstOpenedAt = notification.getOpenedAt();

    notification.markAsOpened(recipientId);
    CreatedAt secondOpenedAt = notification.getOpenedAt();

    assertThat(firstOpenedAt).isEqualTo(secondOpenedAt);
  }

  @Test
  void shouldReconstitueNotificationTest() {
    NotificationId id = NotificationId.generate();
    UserId recipientId = UserId.from(UUID.randomUUID());
    UserId actorId = UserId.from(UUID.randomUUID());
    NotificationReferenceId referenceId = NotificationReferenceId.from(UUID.randomUUID());
    CreatedAt createdAt = CreatedAt.from(LocalDateTime.now());
    CreatedAt readAt = CreatedAt.from(LocalDateTime.now());
    CreatedAt openedAt = CreatedAt.from(LocalDateTime.now());

    NotificationState state = new NotificationState(NotificationStatus.OPENED, readAt, openedAt);

    Notification notification =
        Notification.reconstitute(
            id,
            recipientId,
            actorId,
            NotificationType.SERVICE_PROVIDER_APPROVED,
            ReferenceType.SERVICE_PROVIDER,
            referenceId,
            state,
            createdAt);

    assertThat(notification.getId()).isEqualTo(id);
    assertThat(notification.getRecipientId()).isEqualTo(recipientId);
    assertThat(notification.getActorId()).isEqualTo(actorId);
    assertThat(notification.getStatus()).isEqualTo(NotificationStatus.OPENED);
    assertThat(notification.getReadAt()).isEqualTo(readAt);
    assertThat(notification.getOpenedAt()).isEqualTo(openedAt);
  }
}
