package cm.klg.notification.domaine.notification;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class NotificationStateTest {

  @Test
  void shouldCreateUnreadStateTest() {
    NotificationState state = NotificationState.unread();

    assertThat(state.status()).isEqualTo(NotificationStatus.UNREAD);
    assertThat(state.readAt()).isNull();
    assertThat(state.openedAt()).isNull();
  }

  @Test
  void shouldCreateReadStateTest() {
    CreatedAt readAt = CreatedAt.from(LocalDateTime.now());

    NotificationState state = new NotificationState(NotificationStatus.READ, readAt, null);

    assertThat(state.status()).isEqualTo(NotificationStatus.READ);
    assertThat(state.readAt()).isEqualTo(readAt);
    assertThat(state.openedAt()).isNull();
  }

  @Test
  void shouldCreateOpenedStateWithBothTimestampsTest() {
    CreatedAt readAt = CreatedAt.from(LocalDateTime.now());
    CreatedAt openedAt = CreatedAt.from(LocalDateTime.now());

    NotificationState state = new NotificationState(NotificationStatus.OPENED, readAt, openedAt);

    assertThat(state.status()).isEqualTo(NotificationStatus.OPENED);
    assertThat(state.readAt()).isEqualTo(readAt);
    assertThat(state.openedAt()).isEqualTo(openedAt);
  }
}
