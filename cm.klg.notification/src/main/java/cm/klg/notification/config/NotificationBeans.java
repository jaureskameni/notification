package cm.klg.notification.config;

import cm.klg.common.base.config.TransactionBeansProvider;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.notification.adapter.rest.DefaultDomainToHttpExceptionTranslator;
import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.application.usecase.CreateNewUserUseCase;
import cm.klg.notification.application.usecase.CreateNotificationUseCase;
import cm.klg.notification.application.usecase.GetMyNotificationsUseCase;
import cm.klg.notification.application.usecase.GetMyUnreadNotificationCountUseCase;
import cm.klg.notification.application.usecase.MarkAllMyNotificationsAsReadUseCase;
import cm.klg.notification.application.usecase.OpenMyNotificationUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class NotificationBeans implements TransactionBeansProvider {

  @Bean
  public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
    return new JwtGrantedAuthoritiesConverter();
  }

  @Bean
  public DomainToHttpExceptionTranslator domainToHttpExceptionTranslator() {
    return new DefaultDomainToHttpExceptionTranslator();
  }

  @Bean
  public CreateNewUserUseCase createNewUserUseCase(UserRepository userRepository) {
    return new CreateNewUserUseCase(userRepository);
  }

  @Bean
  public CreateNotificationUseCase createNotificationUseCase(
      NotificationRepository notificationRepository) {
    return new CreateNotificationUseCase(notificationRepository);
  }

  @Bean
  public GetMyNotificationsUseCase getMyNotificationsUseCase(
      NotificationRepository notificationRepository) {
    return new GetMyNotificationsUseCase(notificationRepository);
  }

  @Bean
  public GetMyUnreadNotificationCountUseCase getMyUnreadNotificationCountUseCase(
      NotificationRepository notificationRepository) {
    return new GetMyUnreadNotificationCountUseCase(notificationRepository);
  }

  @Bean
  public OpenMyNotificationUseCase openMyNotificationUseCase(
      NotificationRepository notificationRepository) {
    return new OpenMyNotificationUseCase(notificationRepository);
  }

  @Bean
  public MarkAllMyNotificationsAsReadUseCase markAllMyNotificationsAsReadUseCase(
      NotificationRepository notificationRepository) {
    return new MarkAllMyNotificationsAsReadUseCase(notificationRepository);
  }
}
