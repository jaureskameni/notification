package cm.klg.notification.application.usecase;

import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.notification.NotificationId;
import cm.klg.notification.domaine.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OpenMyNotificationUseCase {
  private final NotificationRepository notificationRepository;

  public void execute(Command command) {
    var notification = notificationRepository.load(command.notificationId());
    notification.markAsOpened(command.recipientId());
    notificationRepository.update(notification);
  }

  public record Command(UserId recipientId, NotificationId notificationId) {}
}
