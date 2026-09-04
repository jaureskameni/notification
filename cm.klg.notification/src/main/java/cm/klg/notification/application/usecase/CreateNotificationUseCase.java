package cm.klg.notification.application.usecase;

import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.notification.Notification;
import cm.klg.notification.domaine.notification.NotificationReferenceId;
import cm.klg.notification.domaine.notification.NotificationType;
import cm.klg.notification.domaine.notification.ReferenceType;
import cm.klg.notification.domaine.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateNotificationUseCase {
  private final NotificationRepository notificationRepository;

  public void execute(CreateNotificationCommand command) {
    Notification notification =
        Notification.of(
            command.recipientId(),
            command.actorId(),
            command.notificationType(),
            command.referenceType(),
            command.referenceId());
    notificationRepository.insert(notification);
  }

  public record CreateNotificationCommand(
      UserId recipientId,
      UserId actorId,
      NotificationType notificationType,
      ReferenceType referenceType,
      NotificationReferenceId referenceId) {}
}
