package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.notification.application.usecase.CreateNewUserUseCase;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import cm.klg.notification.application.usecase.DeleteUserUseCase;
import cm.klg.notification.application.usecase.UpdateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingInboundSpringBeans {

  @Bean
  public CreateUserInboundEventHandler createUserInboundEventHandler(
      CreateNewUserUseCase createNewUserUseCase, MessagingInboundMapper messagingInboundMapper) {
    return new CreateUserInboundEventHandler(createNewUserUseCase, messagingInboundMapper);
  }

  @Bean
  public UpdateUserInboundEventHandler updateUserInboundEventHandler(
      UpdateUserUseCase updateUserUseCase, MessagingInboundMapper messagingInboundMapper) {
    return new UpdateUserInboundEventHandler(updateUserUseCase, messagingInboundMapper);
  }

  @Bean
  public DeleteUserInboundEventHandler deleteUserInboundEventHandler(
      DeleteUserUseCase deleteUserUseCase) {
    return new DeleteUserInboundEventHandler(deleteUserUseCase);
  }

  @Bean
  public CreateServiceProviderInboundEventHandler createServiceProviderInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new CreateServiceProviderInboundEventHandler(
        createNotificationUseCase, messagingInboundMapper);
  }

  @Bean
  public ApproveServiceProviderInboundEventHandler approveServiceProviderInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new ApproveServiceProviderInboundEventHandler(
        createNotificationUseCase, messagingInboundMapper);
  }

  @Bean
  public RejectServiceProviderInboundEventHandler rejectServiceProviderInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new RejectServiceProviderInboundEventHandler(
        createNotificationUseCase, messagingInboundMapper);
  }

  @Bean
  public ServiceRequestCreatedInboundEventHandler serviceRequestCreatedInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new ServiceRequestCreatedInboundEventHandler(
        createNotificationUseCase, messagingInboundMapper);
  }

  @Bean
  public ServiceRequestAcceptedInboundEventHandler serviceRequestAcceptedInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new ServiceRequestAcceptedInboundEventHandler(
        createNotificationUseCase, messagingInboundMapper);
  }

  @Bean
  public ServiceRequestRejectedInboundEventHandler serviceRequestRejectedInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new ServiceRequestRejectedInboundEventHandler(
        createNotificationUseCase, messagingInboundMapper);
  }

  @Bean
  public ServiceRequestCancelledInboundEventHandler serviceRequestCancelledInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper) {
    return new ServiceRequestCancelledInboundEventHandler(
        createNotificationUseCase, messagingInboundMapper);
  }
}
