package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
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
      CreateNewUserUseCase createNewUserUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new CreateUserInboundEventHandler(
        createNewUserUseCase, messagingInboundMapper, useCaseExecutor);
  }

  @Bean
  public UpdateUserInboundEventHandler updateUserInboundEventHandler(
      UpdateUserUseCase updateUserUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new UpdateUserInboundEventHandler(
        updateUserUseCase, messagingInboundMapper, useCaseExecutor);
  }

  @Bean
  public DeleteUserInboundEventHandler deleteUserInboundEventHandler(
      DeleteUserUseCase deleteUserUseCase, UseCaseExecutor useCaseExecutor) {
    return new DeleteUserInboundEventHandler(deleteUserUseCase, useCaseExecutor);
  }

  @Bean
  public CreateServiceProviderInboundEventHandler createServiceProviderInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new CreateServiceProviderInboundEventHandler(
        createNotificationUseCase, useCaseExecutor, messagingInboundMapper);
  }

  @Bean
  public ApproveServiceProviderInboundEventHandler approveServiceProviderInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new ApproveServiceProviderInboundEventHandler(
        createNotificationUseCase, useCaseExecutor, messagingInboundMapper);
  }

  @Bean
  public RejectServiceProviderInboundEventHandler rejectServiceProviderInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new RejectServiceProviderInboundEventHandler(
        createNotificationUseCase, useCaseExecutor, messagingInboundMapper);
  }

  @Bean
  public ServiceRequestCreatedInboundEventHandler serviceRequestCreatedInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new ServiceRequestCreatedInboundEventHandler(
        createNotificationUseCase, useCaseExecutor, messagingInboundMapper);
  }

  @Bean
  public ServiceRequestAcceptedInboundEventHandler serviceRequestAcceptedInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new ServiceRequestAcceptedInboundEventHandler(
        createNotificationUseCase, useCaseExecutor, messagingInboundMapper);
  }

  @Bean
  public ServiceRequestRejectedInboundEventHandler serviceRequestRejectedInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new ServiceRequestRejectedInboundEventHandler(
        createNotificationUseCase, useCaseExecutor, messagingInboundMapper);
  }

  @Bean
  public ServiceRequestCancelledInboundEventHandler serviceRequestCancelledInboundEventHandler(
      CreateNotificationUseCase createNotificationUseCase,
      MessagingInboundMapper messagingInboundMapper,
      UseCaseExecutor useCaseExecutor) {
    return new ServiceRequestCancelledInboundEventHandler(
        createNotificationUseCase, useCaseExecutor, messagingInboundMapper);
  }
}
