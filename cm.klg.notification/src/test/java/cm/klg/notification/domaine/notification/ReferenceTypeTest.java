package cm.klg.notification.domaine.notification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReferenceTypeTest {

  @Test
  void shouldHaveTwoReferenceTypes() {
    assertThat(ReferenceType.values())
        .containsExactly(ReferenceType.SERVICE_REQUEST, ReferenceType.SERVICE_PROVIDER);
  }

  @Test
  void shouldParseFromString() {
    assertThat(ReferenceType.valueOf("SERVICE_REQUEST")).isEqualTo(ReferenceType.SERVICE_REQUEST);
    assertThat(ReferenceType.valueOf("SERVICE_PROVIDER")).isEqualTo(ReferenceType.SERVICE_PROVIDER);
  }
}
