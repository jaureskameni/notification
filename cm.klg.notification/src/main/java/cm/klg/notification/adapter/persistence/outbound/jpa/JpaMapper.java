package cm.klg.notification.adapter.persistence.outbound.jpa;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.notification.domaine.notification.Notification;
import cm.klg.notification.domaine.notification.NotificationId;
import cm.klg.notification.domaine.notification.NotificationReferenceId;
import cm.klg.notification.domaine.notification.NotificationState;
import cm.klg.notification.domaine.notification.NotificationStatus;
import cm.klg.notification.domaine.notification.NotificationType;
import cm.klg.notification.domaine.notification.ReferenceType;
import cm.klg.notification.domaine.user.Email;
import cm.klg.notification.domaine.user.Firstname;
import cm.klg.notification.domaine.user.IdentityId;
import cm.klg.notification.domaine.user.Lastname;
import cm.klg.notification.domaine.user.PhoneNumber;
import cm.klg.notification.domaine.user.User;
import cm.klg.notification.domaine.user.UserId;
import cm.klg.notification.domaine.user.UserProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface JpaMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "identityId", source = "identityId.value")
  @Mapping(target = "lastname", source = "lastname.value")
  @Mapping(target = "firstname", source = "firstname.value")
  @Mapping(target = "email", source = "email.value")
  @Mapping(target = "phoneNumber.countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber.number", source = "phoneNumber.number")
  @Mapping(target = "createdAt", source = "createdAt.value")
  UserJpa toUserJpa(User user);

  default User toNotificationDomain(UserJpa userJpa) {
    PhoneNumber phoneNumber =
        new PhoneNumber(
            userJpa.getPhoneNumber().getCountryCode(), userJpa.getPhoneNumber().getNumber());
    UserProfile userProfile =
        new UserProfile(
            Firstname.from(userJpa.getFirstname()),
            Lastname.from(userJpa.getLastname()),
            phoneNumber,
            Email.from(userJpa.getEmail()));
    return User.reconstitute(
        UserId.from(userJpa.getId()),
        IdentityId.from(userJpa.getIdentityId()),
        userProfile,
        CreatedAt.from(userJpa.getCreatedAt()));
  }

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "recipientId", source = "recipientId.value")
  @Mapping(target = "actorId", source = "actorId.value")
  @Mapping(target = "notificationType", source = "notificationType")
  @Mapping(target = "referenceType", source = "referenceType")
  @Mapping(target = "referenceId", source = "referenceId.value")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "readAt", source = "readAt.value")
  @Mapping(target = "openedAt", source = "openedAt.value")
  NotificationJpa fromNotificationDomain(Notification notification);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "recipientId", source = "recipientId.value")
  @Mapping(target = "actorId", source = "actorId.value")
  @Mapping(target = "notificationType", source = "notificationType")
  @Mapping(target = "referenceType", source = "referenceType")
  @Mapping(target = "referenceId", source = "referenceId.value")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "createdAt", source = "createdAt.value")
  @Mapping(target = "readAt", source = "readAt.value")
  @Mapping(target = "openedAt", source = "openedAt.value")
  void fromNotificationDomain(
      @MappingTarget NotificationJpa notificationJpa, Notification notification);

  default Notification toNotificationDomain(NotificationJpa notificationJpa) {
    NotificationState state =
        new NotificationState(
            NotificationStatus.valueOf(notificationJpa.getStatus()),
            notificationJpa.getReadAt() == null
                ? null
                : CreatedAt.from(notificationJpa.getReadAt()),
            notificationJpa.getOpenedAt() == null
                ? null
                : CreatedAt.from(notificationJpa.getOpenedAt()));
    return Notification.reconstitute(
        NotificationId.from(notificationJpa.getId()),
        UserId.from(notificationJpa.getRecipientId()),
        UserId.from(notificationJpa.getActorId()),
        NotificationType.valueOf(notificationJpa.getNotificationType()),
        ReferenceType.valueOf(notificationJpa.getReferenceType()),
        NotificationReferenceId.from(notificationJpa.getReferenceId()),
        state,
        CreatedAt.from(notificationJpa.getCreatedAt()));
  }
}
