package cm.klg.notification.domaine.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserProfileTest {

  @Test
  void shouldHaveAllFieldsWhenProvided() {
    var firstname = Firstname.from("John");
    var lastname = Lastname.from("Doe");
    var phoneNumber = PhoneNumber.from("+237", "699999999");
    var email = Email.from("john@example.com");

    var profile = new UserProfile(firstname, lastname, phoneNumber, email);

    assertThat(profile)
        .extracting(
            UserProfile::firstname,
            UserProfile::lastname,
            UserProfile::phoneNumber,
            UserProfile::email)
        .containsExactly(firstname, lastname, phoneNumber, email);
  }

  @Test
  void shouldAllowNullFirstname() {
    var profile =
        new UserProfile(
            null,
            Lastname.from("Doe"),
            PhoneNumber.from("+237", "699999999"),
            Email.from("john@example.com"));

    assertThat(profile.firstname()).isNull();
    assertThat(profile.lastname()).isNotNull();
  }

  @Test
  void shouldAllowNullEmail() {
    var profile =
        new UserProfile(
            Firstname.from("John"),
            Lastname.from("Doe"),
            PhoneNumber.from("+237", "699999999"),
            null);

    assertThat(profile.email()).isNull();
    assertThat(profile.lastname()).isNotNull();
  }

  @Test
  void shouldBeEqualWithSameValues() {
    var firstname = Firstname.from("John");
    var lastname = Lastname.from("Doe");
    var phone = PhoneNumber.from("+237", "699999999");
    var email = Email.from("john@example.com");

    assertThat(new UserProfile(firstname, lastname, phone, email))
        .isEqualTo(new UserProfile(firstname, lastname, phone, email));
  }
}
