package cm.klg.notification.adapter.messaging.inbound;

import cm.klg.common.base.transaction.UseCaseExecutor;
import cm.klg.notification.application.usecase.CreateNewUserUseCase;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
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
}
