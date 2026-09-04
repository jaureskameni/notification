package cm.klg.notification.application.usecase;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarkAllMyNotificationsAsReadUseCaseTest {

  @Mock private NotificationRepository notificationRepository;

  @InjectMocks private MarkAllMyNotificationsAsReadUseCase markAllMyNotificationsAsReadUseCase;

  @Test
  void shouldMarkAllUnreadNotificationsAsRead() {
    var recipientId = UserId.from(UUID.randomUUID());

    markAllMyNotificationsAsReadUseCase.execute(recipientId);

    verify(notificationRepository).markAllUnreadAsRead(recipientId);
  }

  @Test
  void shouldHandleMultipleCallsForSameUser() {
    var recipientId = UserId.from(UUID.randomUUID());

    markAllMyNotificationsAsReadUseCase.execute(recipientId);
    markAllMyNotificationsAsReadUseCase.execute(recipientId);

    verify(notificationRepository, times(2)).markAllUnreadAsRead(recipientId);
  }
}
