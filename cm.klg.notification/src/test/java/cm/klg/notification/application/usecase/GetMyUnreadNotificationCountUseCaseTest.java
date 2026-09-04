package cm.klg.notification.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.notification.NotificationStatus;
import cm.klg.notification.domaine.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetMyUnreadNotificationCountUseCaseTest {

  @Mock private NotificationRepository notificationRepository;

  @InjectMocks private GetMyUnreadNotificationCountUseCase getMyUnreadNotificationCountUseCase;

  @Test
  void shouldCountUnreadNotificationsTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    GetMyUnreadNotificationCountUseCase.Command command =
        new GetMyUnreadNotificationCountUseCase.Command(recipientId, NotificationStatus.UNREAD);

    when(notificationRepository.countByRecipientIdAndStatus(recipientId, NotificationStatus.UNREAD))
        .thenReturn(5L);

    long count = getMyUnreadNotificationCountUseCase.execute(command);

    assertThat(count).isEqualTo(5);
    verify(notificationRepository)
        .countByRecipientIdAndStatus(recipientId, NotificationStatus.UNREAD);
  }

  @Test
  void shouldReturnZeroWhenNoNotificationsTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    GetMyUnreadNotificationCountUseCase.Command command =
        new GetMyUnreadNotificationCountUseCase.Command(recipientId, NotificationStatus.UNREAD);

    when(notificationRepository.countByRecipientIdAndStatus(recipientId, NotificationStatus.UNREAD))
        .thenReturn(0L);

    long count = getMyUnreadNotificationCountUseCase.execute(command);

    assertThat(count).isZero();
  }

  @Test
  void shouldCountNotificationsByDifferentStatusTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    GetMyUnreadNotificationCountUseCase.Command command =
        new GetMyUnreadNotificationCountUseCase.Command(recipientId, NotificationStatus.READ);

    when(notificationRepository.countByRecipientIdAndStatus(recipientId, NotificationStatus.READ))
        .thenReturn(3L);

    long count = getMyUnreadNotificationCountUseCase.execute(command);

    assertThat(count).isEqualTo(3);
    verify(notificationRepository)
        .countByRecipientIdAndStatus(recipientId, NotificationStatus.READ);
  }

  @Test
  void shouldHandleNullStatusTest() {
    UserId recipientId = UserId.from(UUID.randomUUID());
    GetMyUnreadNotificationCountUseCase.Command command =
        new GetMyUnreadNotificationCountUseCase.Command(recipientId, null);

    when(notificationRepository.countByRecipientIdAndStatus(recipientId, null)).thenReturn(10L);

    long count = getMyUnreadNotificationCountUseCase.execute(command);

    assertThat(count).isEqualTo(10);
  }
}
