package cm.klg.notification.domaine.notification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RejectionReasonTest {

  @Test
  void shouldUseNullSentinelForNullValue() {
    assertThat(RejectionReason.from(null)).isSameAs(RejectionReason.NULL);
  }

  @Test
  void shouldPreserveReasonValue() {
    assertThat(RejectionReason.from("CNI_EXPIRED").value()).isEqualTo("CNI_EXPIRED");
  }
}
