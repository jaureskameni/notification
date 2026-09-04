package cm.klg.notification.domaine.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PhoneNumberTest {

  @Test
  void shouldCreatePhoneNumberWithCountryCodeAndNumberTest() {
    PhoneNumber phoneNumber = PhoneNumber.from("+237", "699999999");

    assertThat(phoneNumber.countryCode()).isEqualTo("+237");
    assertThat(phoneNumber.number()).isEqualTo("699999999");
  }

  @Test
  void shouldConcatenateCountryCodeAndNumberInValueMethodTest() {
    PhoneNumber phoneNumber = PhoneNumber.from("+237", "699999999");

    assertThat(phoneNumber.value()).isEqualTo("+237699999999");
  }

  @Test
  void shouldHandleDifferentCountryCodesTest() {
    PhoneNumber phoneNumber = PhoneNumber.from("+1", "2125551234");

    assertThat(phoneNumber.value()).isEqualTo("+12125551234");
  }

  @Test
  void shouldHandleEmptyNumberTest() {
    PhoneNumber phoneNumber = PhoneNumber.from("+237", "");

    assertThat(phoneNumber.value()).isEqualTo("+237");
  }
}
