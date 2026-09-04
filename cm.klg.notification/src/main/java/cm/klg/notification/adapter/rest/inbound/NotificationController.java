package cm.klg.notification.adapter.rest.inbound;

import static org.springframework.http.HttpStatus.OK;

import cm.klg.common.base.adapter.inbound.rest.WithAuthenticationSupport;
import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.generated.notifications.adapter.rest.inbound.api.NotificationsApi;
import cm.klg.generated.notifications.adapter.rest.inbound.dto.NotificationCountDTO;
import cm.klg.generated.notifications.adapter.rest.inbound.dto.NotificationPageDTO;
import cm.klg.generated.notifications.adapter.rest.inbound.dto.NotificationStatusDTO;
import cm.klg.notification.application.usecase.GetMyNotificationsUseCase;
import cm.klg.notification.application.usecase.GetMyUnreadNotificationCountUseCase;
import cm.klg.notification.application.usecase.MarkAllMyNotificationsAsReadUseCase;
import cm.klg.notification.application.usecase.OpenMyNotificationUseCase;
import cm.klg.notification.domaine.notification.NotificationId;
import cm.klg.notification.domaine.user.UserId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController implements NotificationsApi, WithAuthenticationSupport {

  private final UseCaseExecutor useCaseExecutor;
  private final RestMapper restMapper;
  private final GetMyNotificationsUseCase getMyNotificationsUseCase;
  private final GetMyUnreadNotificationCountUseCase getMyUnreadNotificationCountUseCase;
  private final OpenMyNotificationUseCase openMyNotificationUseCase;
  private final MarkAllMyNotificationsAsReadUseCase markAllMyNotificationsAsReadUseCase;

  @Override
  public ResponseEntity<NotificationPageDTO> getMyNotifications(
      Integer limit, NotificationStatusDTO status, Integer page) {
    var result =
        useCaseExecutor.executeQuery(
            () ->
                getMyNotificationsUseCase.execute(
                    restMapper.toMyNotificationquery(getCurrentUserId(), status, limit, page)));
    return ResponseEntity.status(OK).body(restMapper.toNotificationPageDTO(result));
  }

  @Override
  public ResponseEntity<NotificationCountDTO> getMyUnreadNotificationCount(
      NotificationStatusDTO status) {
    long result =
        useCaseExecutor.executeQuery(
            () ->
                getMyUnreadNotificationCountUseCase.execute(
                    restMapper.toCountNotificationCommand(getCurrentUserId(), status)));
    return ResponseEntity.status(OK).body(new NotificationCountDTO().numberOfNotification(result));
  }

  @Override
  public ResponseEntity<Void> openMyNotification(UUID notificationId) {
    useCaseExecutor.runCommand(
        () ->
            openMyNotificationUseCase.execute(
                restMapper.toOpenMyNotificationCommand(
                    getCurrentUserId(), NotificationId.from(notificationId))));
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> markAllMyNotificationsAsRead() {
    useCaseExecutor.runCommand(
        () -> markAllMyNotificationsAsReadUseCase.execute(getCurrentUserId()));
    return ResponseEntity.noContent().build();
  }

  private UserId getCurrentUserId() {
    UUID userId = getCurrentUser().getId().map(UUID::fromString).orElseThrow();
    return UserId.from(userId);
  }
}
