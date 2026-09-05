package cm.klg.notification.domaine.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LastnameTest {

  @Test
  void shouldCreateLastnameWithValidValueTest() {
    Lastname lastname = Lastname.from("Doe");

    assertThat(lastname).isNotNull();
    assertThat(lastname.value()).isEqualTo("Doe");
  }

  @Test
  void shouldPreserveCaseInLastnameTest() {
    Lastname lastname = Lastname.from("DOE");

    assertThat(lastname.value()).isEqualTo("DOE");
  }

  @Test
  void shouldPreserveWhitespaceInLastnameTest() {
    Lastname lastname = Lastname.from("  Doe  ");

    assertThat(lastname.value()).isEqualTo("  Doe  ");
  }
}
