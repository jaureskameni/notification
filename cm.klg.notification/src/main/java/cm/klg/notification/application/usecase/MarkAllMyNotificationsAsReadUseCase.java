package cm.klg.notification.application.usecase;

import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MarkAllMyNotificationsAsReadUseCase {
  private final NotificationRepository notificationRepository;

  public void execute(UserId recipientId) {
    notificationRepository.markAllUnreadAsRead(recipientId);
  }
}
