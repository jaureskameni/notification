package cm.klg.notification.adapter.persistence.outbound.jpa;

import cm.klg.notification.application.NotificationViews;
import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.notification.Notification;
import cm.klg.notification.domaine.notification.NotificationId;
import cm.klg.notification.domaine.notification.NotificationNotFoundException;
import cm.klg.notification.domaine.notification.NotificationStatus;
import cm.klg.notification.domaine.user.UserId;
import cm.klg.notification.utils.PageData;
import cm.klg.notification.utils.PaginationFetchRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
class NotificationJpaRepository implements NotificationRepository {

  private final NotificationSpringRepository notificationSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insert(@NonNull Notification notification) {
    notificationSpringRepository.save(jpaMapper.fromNotificationDomain(notification));
  }

  @Override
  public void update(@NonNull Notification notification) {
    notificationSpringRepository
        .findAggregateById(notification.getId().value())
        .ifPresentOrElse(
            notificationJpa -> {
              jpaMapper.fromNotificationDomain(notificationJpa, notification);
              notificationSpringRepository.save(notificationJpa);
            },
            () -> {
              throw new NotificationNotFoundException();
            });
  }

  @Override
  public Notification load(@NonNull NotificationId notificationId) {
    return loadById(notificationId).orElseThrow(NotificationNotFoundException::new);
  }

  @Override
  public long countByRecipientIdAndStatus(@NonNull UserId recipientId, NotificationStatus status) {

    return notificationSpringRepository.countByRecipientIdAndStatus(
        recipientId.value(), status.name());
  }

  @Override
  public void markAllUnreadAsRead(@NonNull UserId recipientId) {
    notificationSpringRepository.markAllUnreadAsRead(recipientId.value(), LocalDateTime.now());
  }

  @Override
  public PageData<NotificationViews> loadAllMyNotificationsAsView(
      @NonNull UserId recipientId, @NonNull PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    var serviceRequestPage =
        notificationSpringRepository.findAllMyNotificationByRecipientIdAsView1(
            recipientId.value(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  @Override
  public PageData<NotificationViews> loadAllMyNotificationByStatusAsView1(
      UserId recipientId, NotificationStatus status, PaginationFetchRequest pagination) {
    Pageable pageable = PageRequest.of(pagination.pageIndex(), pagination.limit());
    var serviceRequestPage =
        notificationSpringRepository.findAllNotificationByRecipientIdAndStatusAsView1(
            recipientId.value(), status.name(), pageable);
    return new PageData<>(serviceRequestPage.getTotalElements(), serviceRequestPage.getContent());
  }

  private Optional<Notification> loadById(@NonNull NotificationId notificationId) {
    return notificationSpringRepository
        .findById(notificationId.value())
        .map(jpaMapper::toNotificationDomain);
  }
}
