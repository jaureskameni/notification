package cm.klg.notification.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.notification.domaine.notification.Notification;
import cm.klg.notification.domaine.notification.NotificationReferenceId;
import cm.klg.notification.domaine.notification.NotificationType;
import cm.klg.notification.domaine.notification.ReferenceType;
import cm.klg.notification.domaine.notification.RejectionReason;
import cm.klg.notification.domaine.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class JpaMapperTest {

  private final JpaMapper mapper = Mappers.getMapper(JpaMapper.class);

  @Test
  void shouldPersistAndRestoreRejectionReason() {
    var notification =
        Notification.of(
            UserId.from(UUID.randomUUID()),
            UserId.from(UUID.randomUUID()),
            NotificationType.SERVICE_PROVIDER_REJECTED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()),
            RejectionReason.from("CNI_EXPIRED"));

    var notificationJpa = mapper.fromNotificationDomain(notification);
    var restoredNotification = mapper.toNotificationDomain(notificationJpa);

    assertThat(notificationJpa.getReason()).isEqualTo("CNI_EXPIRED");
    assertThat(restoredNotification.getReason()).isEqualTo(RejectionReason.from("CNI_EXPIRED"));
  }

  @Test
  void shouldPersistNullSentinelAsNull() {
    var notification =
        Notification.of(
            UserId.from(UUID.randomUUID()),
            UserId.from(UUID.randomUUID()),
            NotificationType.SERVICE_PROVIDER_APPROVED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(UUID.randomUUID()));

    var notificationJpa = mapper.fromNotificationDomain(notification);
    var restoredNotification = mapper.toNotificationDomain(notificationJpa);

    assertThat(notificationJpa.getReason()).isNull();
    assertThat(restoredNotification.getReason()).isSameAs(RejectionReason.NULL);
  }
}
