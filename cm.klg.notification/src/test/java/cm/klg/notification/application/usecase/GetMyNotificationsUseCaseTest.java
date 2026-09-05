package cm.klg.notification.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.notification.application.NotificationViews;
import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.notification.NotificationStatus;
import cm.klg.notification.domaine.user.UserId;
import cm.klg.notification.utils.PageData;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMyNotificationsUseCaseTest {

  @Mock private NotificationRepository notificationRepository;

  @InjectMocks private GetMyNotificationsUseCase getMyNotificationsUseCase;

  @Test
  void shouldFetchAllNotificationsWhenStatusIsNullTest() {
    var recipientId = UserId.from(UUID.randomUUID());
    var query = new GetMyNotificationsUseCase.Query(recipientId, null, 10, 0);

    var views1 = createNotificationView();
    var views2 = createNotificationView();
    var pageData = PageData.of(2L, List.of(views1, views2));

    when(notificationRepository.loadAllMyNotificationsAsView(eq(recipientId), any()))
        .thenReturn(pageData);

    var response = getMyNotificationsUseCase.execute(query);

    assertThat(response.notificationViews()).hasSize(2);
    assertThat(response.count()).isEqualTo(2);
    verify(notificationRepository).loadAllMyNotificationsAsView(eq(recipientId), any());
  }

  @Test
  void shouldFetchNotificationsByStatusWhenStatusIsProvidedTest() {
    var recipientId = UserId.from(UUID.randomUUID());
    var query = new GetMyNotificationsUseCase.Query(recipientId, NotificationStatus.UNREAD, 10, 0);

    var views = createNotificationView();
    var pageData = PageData.of(1L, List.of(views));

    when(notificationRepository.loadAllMyNotificationByStatusAsView1(
            eq(recipientId), eq(NotificationStatus.UNREAD), any()))
        .thenReturn(pageData);

    var response = getMyNotificationsUseCase.execute(query);

    assertThat(response.notificationViews()).hasSize(1);
    assertThat(response.count()).isEqualTo(1);
    verify(notificationRepository)
        .loadAllMyNotificationByStatusAsView1(
            eq(recipientId), eq(NotificationStatus.UNREAD), any());
  }

  @Test
  void shouldHandleEmptyNotificationsListTest() {
    var recipientId = UserId.from(UUID.randomUUID());
    var query = new GetMyNotificationsUseCase.Query(recipientId, null, 10, 0);

    var pageData = PageData.<NotificationViews>of(0L, List.of());

    when(notificationRepository.loadAllMyNotificationsAsView(eq(recipientId), any()))
        .thenReturn(pageData);

    var response = getMyNotificationsUseCase.execute(query);

    assertThat(response.notificationViews()).isEmpty();
    assertThat(response.count()).isZero();
  }

  private NotificationViews createNotificationView() {
    return mock(NotificationViews.class);
  }
}
