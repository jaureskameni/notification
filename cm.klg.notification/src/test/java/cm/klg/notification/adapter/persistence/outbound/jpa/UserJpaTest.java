package cm.klg.notification.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.entity.PhoneNumberJpa;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserJpaTest {

  @Test
  void shouldCreateWithAllFields() {
    var userId = UUID.randomUUID();
    var identityId = UUID.randomUUID();
    var phone = new PhoneNumberJpa("+237", "699999999");
    var createdAt = LocalDateTime.now();

    var user = new UserJpa();
    user.setId(userId);
    user.setIdentityId(identityId);
    user.setFirstname("John");
    user.setLastname("Doe");
    user.setEmail("john@example.com");
    user.setPhoneNumber(phone);
    user.setCreatedAt(createdAt);

    assertThat(user)
        .extracting(UserJpa::getId, UserJpa::getFirstname, UserJpa::getLastname, UserJpa::getEmail)
        .containsExactly(userId, "John", "Doe", "john@example.com");
  }

  @Test
  void shouldAllowNullEmail() {
    var user = new UserJpa();
    user.setId(UUID.randomUUID());
    user.setFirstname("Jane");
    user.setLastname("Smith");
    user.setEmail(null);

    assertThat(user.getEmail()).isNull();
    assertThat(user.getFirstname()).isEqualTo("Jane");
  }

  @Test
  void shouldAllowNullFirstname() {
    var user = new UserJpa();
    user.setId(UUID.randomUUID());
    user.setFirstname(null);
    user.setLastname("Doe");

    assertThat(user.getFirstname()).isNull();
    assertThat(user.getLastname()).isEqualTo("Doe");
  }

  @Test
  void shouldBeEqualByIdOnly() {
    var userId = UUID.randomUUID();
    var u1 = new UserJpa();
    u1.setId(userId);
    u1.setFirstname("John");

    var u2 = new UserJpa();
    u2.setId(userId);
    u2.setFirstname("Jane");

    assertThat(u1).isEqualTo(u2);
  }

  @Test
  void shouldNotBeEqualWithDifferentIds() {
    var u1 = new UserJpa();
    u1.setId(UUID.randomUUID());

    var u2 = new UserJpa();
    u2.setId(UUID.randomUUID());

    assertThat(u1).isNotEqualTo(u2);
  }

  @Test
  void shouldHaveConsistentHashCode() {
    var userId = UUID.randomUUID();
    var u1 = new UserJpa();
    u1.setId(userId);

    var u2 = new UserJpa();
    u2.setId(userId);

    assertThat(u1.hashCode()).hasSameHashCodeAs(u2.hashCode());
  }
}
