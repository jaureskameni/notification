package cm.klg.notification.domaine.notification;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class NotificationReferenceIdTest {

  @Test
  void shouldCreateFromUuid() {
    var uuid = UUID.randomUUID();
    var id = NotificationReferenceId.from(uuid);

    assertThat(id.value()).isEqualTo(uuid);
  }

  @Test
  void shouldBeEqualWhenCreatedFromSameUuid() {
    var uuid = UUID.randomUUID();

    assertThat(NotificationReferenceId.from(uuid)).isEqualTo(NotificationReferenceId.from(uuid));
  }

  @Test
  void shouldNotBeEqualWhenCreatedFromDifferentUuids() {
    assertThat(NotificationReferenceId.from(UUID.randomUUID()))
        .isNotEqualTo(NotificationReferenceId.from(UUID.randomUUID()));
  }
}
