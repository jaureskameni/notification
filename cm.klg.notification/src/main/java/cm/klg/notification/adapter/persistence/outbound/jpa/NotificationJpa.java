package cm.klg.notification.adapter.persistence.outbound.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@SuppressWarnings("JpaDataSourceORMInspection")
@FieldNameConstants
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "t_notification")
class NotificationJpa {
  @Id
  @Column(name = "c_id")
  private UUID id;

  @Column(name = "c_recipient_id")
  private UUID recipientId;

  @Column(name = "c_actor_id")
  private UUID actorId;

  @Column(name = "c_notification_type")
  private String notificationType;

  @Column(name = "c_reference_type")
  private String referenceType;

  @Column(name = "c_reference_id")
  private UUID referenceId;

  @Column(name = "c_status")
  private String status;

  @Column(name = "c_created_at")
  private LocalDateTime createdAt;

  @Column(name = "c_read_at")
  private LocalDateTime readAt;

  @Column(name = "c_opened_at")
  private LocalDateTime openedAt;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    NotificationJpa that = (NotificationJpa) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
