package cm.klg.notification.domaine.notification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NotificationStatusTest {

  @Test
  void shouldHaveThreeStatuses() {
    assertThat(NotificationStatus.values())
        .containsExactly(
            NotificationStatus.UNREAD, NotificationStatus.READ, NotificationStatus.OPENED);
  }

  @Test
  void shouldParseFromString() {
    assertThat(NotificationStatus.valueOf("UNREAD")).isEqualTo(NotificationStatus.UNREAD);
    assertThat(NotificationStatus.valueOf("READ")).isEqualTo(NotificationStatus.READ);
    assertThat(NotificationStatus.valueOf("OPENED")).isEqualTo(NotificationStatus.OPENED);
  }
}
