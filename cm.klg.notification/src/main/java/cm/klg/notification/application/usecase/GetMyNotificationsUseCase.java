package cm.klg.notification.application.usecase;

import cm.klg.notification.application.NotificationViews;
import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.notification.NotificationStatus;
import cm.klg.notification.domaine.user.UserId;
import cm.klg.notification.utils.PageData;
import cm.klg.notification.utils.PaginationFetchRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMyNotificationsUseCase {
  private final NotificationRepository notificationRepository;

  public Response execute(Query query) {
    var pagination = new PaginationFetchRequest(query.limit(), query.page());

    if (query.status() == null) {
      return toResponse(
          notificationRepository.loadAllMyNotificationsAsView(query.recipientId, pagination));
    }
    return toResponse(
        notificationRepository.loadAllMyNotificationByStatusAsView1(
            query.recipientId, query.status(), pagination));
  }

  private static Response toResponse(PageData<NotificationViews> pageData) {
    return new Response(new ArrayList<>(pageData.elements()), pageData.total());
  }

  public record Query(UserId recipientId, NotificationStatus status, int limit, int page) {}

  public record Response(List<NotificationViews> notificationViews, long count) {}
}
