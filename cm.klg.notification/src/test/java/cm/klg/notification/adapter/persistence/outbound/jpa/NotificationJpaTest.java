package cm.klg.notification.adapter.persistence.outbound.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class NotificationJpaTest {

  @Test
  void shouldCreateWithAllFields() {
    var id = UUID.randomUUID();
    var recipientId = UUID.randomUUID();
    var actorId = UUID.randomUUID();
    var referenceId = UUID.randomUUID();
    var createdAt = LocalDateTime.now();

    var notification = new NotificationJpa();
    notification.setId(id);
    notification.setRecipientId(recipientId);
    notification.setActorId(actorId);
    notification.setNotificationType("SERVICE_PROVIDER_CREATED");
    notification.setReferenceType("SERVICE_PROVIDER");
    notification.setReferenceId(referenceId);
    notification.setStatus("UNREAD");
    notification.setCreatedAt(createdAt);

    assertThat(notification)
        .extracting(
            NotificationJpa::getId,
            NotificationJpa::getRecipientId,
            NotificationJpa::getActorId,
            NotificationJpa::getStatus)
        .containsExactly(id, recipientId, actorId, "UNREAD");
  }

  @Test
  void shouldAllowNullDates() {
    var notification = new NotificationJpa();
    notification.setId(UUID.randomUUID());
    notification.setStatus("UNREAD");
    notification.setReadAt(null);
    notification.setOpenedAt(null);

    assertThat(notification.getReadAt()).isNull();
    assertThat(notification.getOpenedAt()).isNull();
  }

  @Test
  void shouldBeEqualByIdOnly() {
    var id = UUID.randomUUID();
    var n1 = new NotificationJpa();
    n1.setId(id);
    n1.setStatus("UNREAD");

    var n2 = new NotificationJpa();
    n2.setId(id);
    n2.setStatus("READ");

    assertThat(n1).isEqualTo(n2);
  }

  @Test
  void shouldNotBeEqualWithDifferentIds() {
    var n1 = new NotificationJpa();
    n1.setId(UUID.randomUUID());

    var n2 = new NotificationJpa();
    n2.setId(UUID.randomUUID());

    assertThat(n1).isNotEqualTo(n2);
  }

  @Test
  void shouldHaveConsistentHashCode() {
    var id = UUID.randomUUID();
    var n1 = new NotificationJpa();
    n1.setId(id);

    var n2 = new NotificationJpa();
    n2.setId(id);

    assertThat(n1.hashCode()).hasSameHashCodeAs(n2.hashCode());
  }
}
