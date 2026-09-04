package cm.klg.notification.adapter.persistence.outbound.jpa;

import cm.klg.notification.application.NotificationViews;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

interface NotificationSpringRepository extends JpaRepository<NotificationJpa, UUID> {

  Optional<NotificationJpa> findAggregateById(UUID id);

  Page<NotificationJpa> findByRecipientIdAndStatusOrderByCreatedAtDesc(
      UUID recipientId, String status, Pageable pageable);

  Page<NotificationJpa> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId, Pageable pageable);

  long countByRecipientIdAndStatus(UUID recipientId, String status);

  @Modifying
  @Transactional
  @Query(
      "update NotificationJpa n set n.status = 'READ', n.readAt = :readAt "
          + "where n.recipientId = :recipientId and n.status = 'UNREAD'")
  void markAllUnreadAsRead(
      @Param("recipientId") UUID recipientId, @Param("readAt") LocalDateTime readAt);

  @Query(
      value =
          """
          SELECT
              n.id                        AS id,
              n.recipientId               AS recipientId,
              n.actorId                   AS actorId,
              n.referenceId               AS referenceId,
              n.notificationType          AS notificationType,
              n.referenceType             AS referenceType,
              n.status                    AS status,
              n.createdAt                 AS createdAt,
              n.readAt                    AS readAt,
              n.openedAt                  AS openedAt
          FROM NotificationJpa n
          WHERE n.recipientId = :recipientId
          ORDER BY n.createdAt DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT n) FROM NotificationJpa n WHERE" + " n.recipientId = :recipientId")
  Page<NotificationViews> findAllMyNotificationByRecipientIdAsView1(
      UUID recipientId, Pageable pageable);

  @Query(
      value =
          """
          SELECT
              n.id                        AS id,
              n.recipientId               AS recipientId,
              n.actorId                   AS actorId,
              n.referenceId               AS referenceId,
              n.notificationType          AS notificationType,
              n.referenceType             AS referenceType,
              n.status                    AS status,
              n.createdAt                 AS createdAt,
              n.readAt                    AS readAt,
              n.openedAt                  AS openedAt
          FROM NotificationJpa n
          WHERE n.recipientId = :recipientId AND n.status = :status
          ORDER BY n.createdAt DESC\
          """,
      countQuery =
          "SELECT COUNT(DISTINCT n) FROM NotificationJpa n WHERE"
              + " n.recipientId = :recipientId AND n.status = :status")
  Page<NotificationViews> findAllNotificationByRecipientIdAndStatusAsView1(
      UUID recipientId, String status, Pageable pageable);
}
