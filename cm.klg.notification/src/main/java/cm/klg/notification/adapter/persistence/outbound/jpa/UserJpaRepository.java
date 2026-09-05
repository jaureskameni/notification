package cm.klg.notification.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpaConverter;
import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.domaine.user.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository {

  private static final Logger log = LoggerFactory.getLogger(UserJpaRepository.class);

  private static final PhoneNumberJpaConverter PHONE_NUMBER_CONVERTER =
      new PhoneNumberJpaConverter();

  private final UserSpringRepository userSpringRepository;
  private final JpaMapper jpaMapper;

  @Override
  public void insertIfAbsent(@NonNull User user) {
    UserJpa userJpa = jpaMapper.toUserJpa(user);
    int insertedRows =
        userSpringRepository.insertIfAbsent(
            String.valueOf(userJpa.getId()),
            String.valueOf(userJpa.getIdentityId()),
            userJpa.getFirstname(),
            userJpa.getLastname(),
            userJpa.getEmail(),
            PHONE_NUMBER_CONVERTER.convertToDatabaseColumn(userJpa.getPhoneNumber()),
            userJpa.getCreatedAt());
    if (insertedRows == 0) {
      log.debug(
          "User with identityId {} already exists, skipping insertion.",
          user.getIdentityId().value());
    }
  }
}
