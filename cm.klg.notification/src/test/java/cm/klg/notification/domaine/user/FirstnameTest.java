package cm.klg.notification.domaine.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class FirstnameTest {

  @Test
  void shouldCreateFirstnameWithValidValueTest() {
    Firstname firstname = Firstname.from("John");

    assertThat(firstname).isNotNull();
    assertThat(firstname.value()).isEqualTo("John");
  }

  @Test
  void shouldTrimWhitespaceFromFirstnameTest() {
    Firstname firstname = Firstname.from("  John  ");

    assertThat(firstname.value()).isEqualTo("John");
  }

  @Test
  void shouldReturnNullWhenFirstnameIsNullTest() {
    Firstname firstname = Firstname.from(null);

    assertThat(firstname).isNull();
  }

  @Test
  void shouldThrowExceptionWhenFirstnameIsBlankTest() {
    assertThatThrownBy(() -> Firstname.from("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Firstname cannot be blank");
  }

  @Test
  void shouldThrowExceptionWhenFirstnameIsEmptyTest() {
    assertThatThrownBy(() -> Firstname.from(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Firstname cannot be blank");
  }
}
