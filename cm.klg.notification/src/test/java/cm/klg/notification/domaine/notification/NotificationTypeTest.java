package cm.klg.notification.domaine.notification;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NotificationTypeTest {

  @Test
  void shouldHaveSevenTypes() {
    assertThat(NotificationType.values()).hasSize(7);
  }

  @Test
  void shouldContainAllServiceProviderTypes() {
    assertThat(NotificationType.values())
        .contains(
            NotificationType.SERVICE_PROVIDER_CREATED,
            NotificationType.SERVICE_PROVIDER_APPROVED,
            NotificationType.SERVICE_PROVIDER_REJECTED);
  }

  @Test
  void shouldContainAllServiceRequestTypes() {
    assertThat(NotificationType.values())
        .contains(
            NotificationType.SERVICE_REQUEST_CREATED,
            NotificationType.SERVICE_REQUEST_ACCEPTED,
            NotificationType.SERVICE_REQUEST_REJECTED,
            NotificationType.SERVICE_REQUEST_CANCELLED);
  }

  @Test
  void shouldParseFromString() {
    assertThat(NotificationType.valueOf("SERVICE_PROVIDER_APPROVED"))
        .isEqualTo(NotificationType.SERVICE_PROVIDER_APPROVED);
  }
}
