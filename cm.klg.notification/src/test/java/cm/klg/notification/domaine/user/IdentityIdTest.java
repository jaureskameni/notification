package cm.klg.notification.domaine.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class IdentityIdTest {

  @Test
  void shouldCreateFromUuid() {
    var uuid = UUID.randomUUID();
    var id = IdentityId.from(uuid);

    assertThat(id.value()).isEqualTo(uuid);
  }

  @Test
  void shouldCreateFromUserId() {
    var userId = UserId.from(UUID.randomUUID());
    var id = IdentityId.from(userId);

    assertThat(id.value()).isEqualTo(userId.value());
  }

  @Test
  void shouldBeEqualWhenCreatedFromSameUuid() {
    var uuid = UUID.randomUUID();

    assertThat(IdentityId.from(uuid)).isEqualTo(IdentityId.from(uuid));
  }

  @Test
  void shouldBeEqualWhenCreatedFromUserIdAndUuid() {
    var uuid = UUID.randomUUID();
    var userId = UserId.from(uuid);

    assertThat(IdentityId.from(uuid)).isEqualTo(IdentityId.from(userId));
  }

  @Test
  void shouldNotBeEqualWhenCreatedFromDifferentUuids() {
    assertThat(IdentityId.from(UUID.randomUUID())).isNotEqualTo(IdentityId.from(UUID.randomUUID()));
  }
}
