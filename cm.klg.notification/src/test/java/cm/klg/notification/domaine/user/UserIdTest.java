package cm.klg.notification.domaine.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserIdTest {

  @Test
  void shouldCreateFromUuid() {
    var uuid = UUID.randomUUID();
    var id = UserId.from(uuid);

    assertThat(id.value()).isEqualTo(uuid);
  }

  @Test
  void shouldBeEqualWhenCreatedFromSameUuid() {
    var uuid = UUID.randomUUID();

    assertThat(UserId.from(uuid)).isEqualTo(UserId.from(uuid));
  }

  @Test
  void shouldNotBeEqualWhenCreatedFromDifferentUuids() {
    assertThat(UserId.from(UUID.randomUUID())).isNotEqualTo(UserId.from(UUID.randomUUID()));
  }
}
