package cm.klg.notification.domaine.user;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.domain.CreatedAt;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void shouldCreateUserWithCompleteProfileTest() {
    UserId userId = UserId.from(UUID.randomUUID());
    IdentityId identityId = IdentityId.from(UUID.randomUUID());
    Firstname firstname = Firstname.from("John");
    Lastname lastname = Lastname.from("Doe");
    Email email = Email.from("john.doe@example.com");
    PhoneNumber phoneNumber = PhoneNumber.from("+237", "699999999");
    UserProfile userProfile = new UserProfile(firstname, lastname, phoneNumber, email);
    CreatedAt createdAt = CreatedAt.from(LocalDateTime.now());

    User user = new User(userId, identityId, userProfile, createdAt);

    assertThat(user.getId()).isEqualTo(userId);
    assertThat(user.getIdentityId()).isEqualTo(identityId);
    assertThat(user.getFirstname()).isEqualTo(firstname);
    assertThat(user.getLastname()).isEqualTo(lastname);
    assertThat(user.getEmail()).isEqualTo(email);
    assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
    assertThat(user.getCreatedAt()).isEqualTo(createdAt);
  }

  @Test
  void shouldCreateUserWithNullableFirstnameTest() {
    UserId userId = UserId.from(UUID.randomUUID());
    IdentityId identityId = IdentityId.from(UUID.randomUUID());
    Lastname lastname = Lastname.from("Doe");
    PhoneNumber phoneNumber = PhoneNumber.from("+237", "699999999");
    UserProfile userProfile = new UserProfile(null, lastname, phoneNumber, null);
    CreatedAt createdAt = CreatedAt.from(LocalDateTime.now());

    User user = new User(userId, identityId, userProfile, createdAt);

    assertThat(user.getFirstname()).isNull();
    assertThat(user.getEmail()).isNull();
    assertThat(user.getLastname()).isEqualTo(lastname);
  }

  @Test
  void shouldReconstitueUserTest() {
    UserId userId = UserId.from(UUID.randomUUID());
    IdentityId identityId = IdentityId.from(UUID.randomUUID());
    Firstname firstname = Firstname.from("John");
    Lastname lastname = Lastname.from("Doe");
    Email email = Email.from("john.doe@example.com");
    PhoneNumber phoneNumber = PhoneNumber.from("+237", "699999999");
    UserProfile userProfile = new UserProfile(firstname, lastname, phoneNumber, email);
    CreatedAt createdAt = CreatedAt.from(LocalDateTime.now());

    User user = User.reconstitute(userId, identityId, userProfile, createdAt);

    assertThat(user.getId()).isEqualTo(userId);
    assertThat(user.getIdentityId()).isEqualTo(identityId);
    assertThat(user.getFirstname()).isEqualTo(firstname);
  }
}
