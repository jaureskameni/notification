package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.generated.uam.adapter.messaging.inbound.dto.UamUserCreatedEventDTO;
import cm.klg.notification.application.usecase.CreateNewUserUseCase;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import cm.klg.notification.domaine.notification.NotificationReferenceId;
import cm.klg.notification.domaine.notification.NotificationType;
import cm.klg.notification.domaine.notification.ReferenceType;
import cm.klg.notification.domaine.user.UserId;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.ServiceProviderServiceProviderApprovedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderCreatedEventDTO;
import org.openapitools.model.ServiceProviderServiceProviderRejectedEventDTO;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessagingInboundMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  @Mapping(target = "identityId", source = "identityId")
  @Mapping(target = "profile.firstname", source = "firstname")
  @Mapping(target = "profile.lastname", source = "lastname")
  @Mapping(target = "profile.email", source = "email")
  @Mapping(target = "countryCode", source = "phoneNumber.countryCode")
  @Mapping(target = "phoneNumber", source = "phoneNumber.number")
  @Mapping(target = "createdAt", source = "createdAt")
  CreateNewUserUseCase.CreateNewUserCommand toCreateUserCommand(
      UamUserCreatedEventDTO userCreatedEventDTO);

  default CreateNotificationUseCase.CreateNotificationCommand toCreatedNotificationCommand(
      ServiceProviderServiceProviderCreatedEventDTO event) {
    return new CreateNotificationUseCase.CreateNotificationCommand(
        UserId.from(event.getUserId()),
        UserId.from(event.getUserId()),
        NotificationType.SERVICE_PROVIDER_CREATED,
        ReferenceType.SERVICE_PROVIDER,
        NotificationReferenceId.from(event.getServiceProviderId()));
  }

  default CreateNotificationUseCase.CreateNotificationCommand toApprovedNotificationCommand(
      ServiceProviderServiceProviderApprovedEventDTO event) {
    return new CreateNotificationUseCase.CreateNotificationCommand(
        UserId.from(event.getUserId()),
        UserId.from(event.getApprovedBy()),
        NotificationType.SERVICE_PROVIDER_APPROVED,
        ReferenceType.SERVICE_PROVIDER,
        NotificationReferenceId.from(event.getServiceProviderId()));
  }

  default CreateNotificationUseCase.CreateNotificationCommand toRejectedNotificationCommand(
      ServiceProviderServiceProviderRejectedEventDTO event) {
    return new CreateNotificationUseCase.CreateNotificationCommand(
        UserId.from(event.getUserId()),
        UserId.from(event.getRejectedBy()),
        NotificationType.SERVICE_PROVIDER_REJECTED,
        ReferenceType.SERVICE_PROVIDER,
        NotificationReferenceId.from(event.getServiceProviderId()));
  }
}
