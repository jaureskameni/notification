package cm.klg.notification.domaine.notification;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class NotificationIdTest {

  @Test
  void shouldGenerateUniqueNotificationIds() {
    var id1 = NotificationId.generate();
    var id2 = NotificationId.generate();

    assertThat(id1).isNotEqualTo(id2);
  }

  @Test
  void shouldCreateFromUuid() {
    var uuid = UUID.randomUUID();
    var id = NotificationId.from(uuid);

    assertThat(id.value()).isEqualTo(uuid);
  }

  @Test
  void shouldHaveSameValueWhenCreatedFromSameUuid() {
    var uuid = UUID.randomUUID();

    assertThat(NotificationId.from(uuid)).isEqualTo(NotificationId.from(uuid));
  }

  @Test
  void shouldNotBeEqualWhenCreatedFromDifferentUuids() {
    assertThat(NotificationId.from(UUID.randomUUID()))
        .isNotEqualTo(NotificationId.from(UUID.randomUUID()));
  }
}
