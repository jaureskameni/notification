package cm.klg.notification.application.outbound;

import cm.klg.notification.application.NotificationViews;
import cm.klg.notification.domaine.notification.Notification;
import cm.klg.notification.domaine.notification.NotificationId;
import cm.klg.notification.domaine.notification.NotificationStatus;
import cm.klg.notification.domaine.user.UserId;
import cm.klg.notification.utils.PageData;
import cm.klg.notification.utils.PaginationFetchRequest;

public interface NotificationRepository {
  void insert(Notification notification);

  void update(Notification notification);

  Notification load(NotificationId notificationId);

  long countByRecipientIdAndStatus(UserId recipientId, NotificationStatus status);

  void markAllUnreadAsRead(UserId recipientId);

  PageData<NotificationViews> loadAllMyNotificationsAsView(
      UserId recipientId, PaginationFetchRequest pagination);

  PageData<NotificationViews> loadAllMyNotificationByStatusAsView1(
      UserId recipientId, NotificationStatus status, PaginationFetchRequest pagination);
}
