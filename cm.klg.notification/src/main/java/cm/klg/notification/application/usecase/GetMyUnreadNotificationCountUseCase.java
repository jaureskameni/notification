package cm.klg.notification.application.usecase;

import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.notification.NotificationStatus;
import cm.klg.notification.domaine.user.UserId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMyUnreadNotificationCountUseCase {
  private final NotificationRepository notificationRepository;

  public long execute(Command command) {
    return notificationRepository.countByRecipientIdAndStatus(command.recipientId, command.status);
  }

  public record Command(UserId recipientId, NotificationStatus status) {}
}
