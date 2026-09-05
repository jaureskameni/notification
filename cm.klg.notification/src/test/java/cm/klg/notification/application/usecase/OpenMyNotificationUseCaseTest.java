package cm.klg.notification.application.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.notification.Notification;
import cm.klg.notification.domaine.notification.NotificationId;
import cm.klg.notification.domaine.notification.NotificationReferenceId;
import cm.klg.notification.domaine.notification.NotificationType;
import cm.klg.notification.domaine.notification.ReferenceType;
import cm.klg.notification.domaine.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpenMyNotificationUseCaseTest {

  @Mock private NotificationRepository notificationRepository;

  @InjectMocks private OpenMyNotificationUseCase openMyNotificationUseCase;

  @Test
  void shouldOpenNotificationByAuthorizedUserTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    NotificationId notificationId = NotificationId.generate();

    Notification notification =
        Notification.of(
            recipientId,
            UserId.from(UUID.randomUUID()),
            NotificationType.SERVICE_PROVIDER_CREATED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()));

    when(notificationRepository.load(notificationId)).thenReturn(notification);

    OpenMyNotificationUseCase.Command command =
        new OpenMyNotificationUseCase.Command(recipientId, notificationId);

    openMyNotificationUseCase.execute(command);

    ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
    verify(notificationRepository).update(notificationCaptor.capture());

    Notification updatedNotification = notificationCaptor.getValue();
    assert updatedNotification.getOpenedAt() != null;
    assert updatedNotification.getReadAt() != null;
  }

  @Test
  void shouldLoadNotificationByIdTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    NotificationId notificationId = NotificationId.generate();

    Notification notification =
        Notification.of(
            recipientId,
            UserId.from(UUID.randomUUID()),
            NotificationType.SERVICE_PROVIDER_CREATED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()));

    when(notificationRepository.load(notificationId)).thenReturn(notification);

    OpenMyNotificationUseCase.Command command =
        new OpenMyNotificationUseCase.Command(recipientId, notificationId);

    openMyNotificationUseCase.execute(command);

    verify(notificationRepository).load(notificationId);
  }

  @Test
  void shouldUpdateNotificationAfterOpeningTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    NotificationId notificationId = NotificationId.generate();

    Notification notification =
        Notification.of(
            recipientId,
            UserId.from(UUID.randomUUID()),
            NotificationType.SERVICE_PROVIDER_CREATED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()));

    when(notificationRepository.load(notificationId)).thenReturn(notification);

    OpenMyNotificationUseCase.Command command =
        new OpenMyNotificationUseCase.Command(recipientId, notificationId);

    openMyNotificationUseCase.execute(command);

    verify(notificationRepository).update(any(Notification.class));
  }
}
