package cm.klg.notification.domaine.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EmailTest {

  @Test
  void shouldCreateEmailWithValidValueTest() {
    Email email = Email.from("john.doe@example.com");

    assertThat(email).isNotNull();
    assertThat(email.value()).isEqualTo("john.doe@example.com");
  }

  @Test
  void shouldTrimWhitespaceFromEmailTest() {
    Email email = Email.from("  john.doe@example.com  ");

    assertThat(email.value()).isEqualTo("john.doe@example.com");
  }

  @Test
  void shouldThrowExceptionWhenEmailIsNullTest() {
    Email email = Email.from(null);

    assertThat(email).isNull();
  }

  @Test
  void shouldThrowExceptionWhenEmailIsBlankTest() {
    assertThatThrownBy(() -> Email.from("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email cannot be blank");
  }

  @Test
  void shouldThrowExceptionWhenEmailIsEmptyTest() {
    assertThatThrownBy(() -> Email.from(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Email cannot be blank");
  }
}
