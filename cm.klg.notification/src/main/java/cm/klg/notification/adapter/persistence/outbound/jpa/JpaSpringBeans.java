package cm.klg.notification.adapter.persistence.outbound.jpa;

import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.application.outbound.UserRepository;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(
    basePackages = {
      "cm.klg.notification.adapter.persistence.outbound.jpa",
    })
@EnableJpaRepositories(basePackages = {"cm.klg.notification.adapter.persistence.outbound.jpa"})
class JpaSpringBeans {

  @Bean
  public UserRepository userRepository(
      UserSpringRepository userSpringRepository, JpaMapper jpaMapper) {
    return new UserJpaRepository(userSpringRepository, jpaMapper);
  }

  @Bean
  public NotificationRepository notificationRepository(
      NotificationSpringRepository notificationSpringRepository, JpaMapper jpaMapper) {
    return new NotificationJpaRepository(notificationSpringRepository, jpaMapper);
  }
}
