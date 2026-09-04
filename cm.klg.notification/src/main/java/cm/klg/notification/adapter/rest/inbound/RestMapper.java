package cm.klg.notification.adapter.rest.inbound;

import cm.klg.generated.notifications.adapter.rest.inbound.dto.NotificationDTO;
import cm.klg.generated.notifications.adapter.rest.inbound.dto.NotificationPageDTO;
import cm.klg.generated.notifications.adapter.rest.inbound.dto.NotificationStatusDTO;
import cm.klg.generated.notifications.adapter.rest.inbound.dto.NotificationTypeDTO;
import cm.klg.generated.notifications.adapter.rest.inbound.dto.ReferenceTypeDTO;
import cm.klg.notification.application.NotificationViews;
import cm.klg.notification.application.usecase.GetMyNotificationsUseCase;
import cm.klg.notification.application.usecase.GetMyUnreadNotificationCountUseCase;
import cm.klg.notification.application.usecase.OpenMyNotificationUseCase;
import cm.klg.notification.domaine.notification.NotificationId;
import cm.klg.notification.domaine.notification.NotificationStatus;
import cm.klg.notification.domaine.user.UserId;
import org.jspecify.annotations.Nullable;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestMapper {

  default GetMyNotificationsUseCase.Query toMyNotificationquery(
      UserId currentUserId, NotificationStatusDTO status, Integer limit, Integer page) {
    return new GetMyNotificationsUseCase.Query(
        currentUserId, toStatus(status), toLimit(limit), toPage(page));
  }

  private static @Nullable NotificationStatus toStatus(@Nullable NotificationStatusDTO status) {
    return status == null ? null : NotificationStatus.valueOf(status.name());
  }

  private static int toLimit(Integer limit) {
    if (limit != null && limit < 0) {
      throw new IllegalArgumentException("Limit cannot be less than 0");
    } else if (limit != null && limit > 100) {
      return 100;
    } else if (limit == null) {
      return 10;
    }
    return limit;
  }

  private static int toPage(Integer page) {
    if (page != null && page < 0) {
      throw new IllegalArgumentException("Page cannot be less than 0");
    } else if (page == null) {
      return 0;
    }
    return page;
  }

  default NotificationDTO toNotificationDTO(NotificationViews views) {
    NotificationDTO dto =
        new NotificationDTO()
            .id(views.getId())
            .actorId(views.getActorId())
            .notificationType(NotificationTypeDTO.fromValue(views.getNotificationType()))
            .referenceType(ReferenceTypeDTO.fromValue(views.getReferenceType()))
            .referenceId(views.getReferenceId())
            .status(NotificationStatusDTO.fromValue(views.getStatus()))
            .createdAt(views.getCreatedAt());
    if (views.getReadAt() != null) dto.readAt(views.getReadAt());
    if (views.getOpenedAt() != null) dto.openedAt(views.getOpenedAt());
    return dto;
  }

  default NotificationPageDTO toNotificationPageDTO(GetMyNotificationsUseCase.Response result) {
    return new NotificationPageDTO()
        .items(result.notificationViews().stream().map(this::toNotificationDTO).toList())
        .totalOfElements(result.count());
  }

  default OpenMyNotificationUseCase.Command toOpenMyNotificationCommand(
      UserId currentUserId, NotificationId notificationId) {
    return new OpenMyNotificationUseCase.Command(currentUserId, notificationId);
  }

  default GetMyUnreadNotificationCountUseCase.Command toCountNotificationCommand(
      UserId currentUserId, NotificationStatusDTO status) {
    return new GetMyUnreadNotificationCountUseCase.Command(currentUserId, toStatus(status));
  }
}
