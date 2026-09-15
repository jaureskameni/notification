package cm.klg.notification.adapter.persistence.outbound.jpa;

import cm.klg.common.base.entity.PhoneNumberJpaConverter;
import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.domaine.user.User;
import cm.klg.notification.domaine.user.UserId;
import java.util.Optional;
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
            userJpa.getFirstname(),
            userJpa.getLastname(),
            userJpa.getEmail(),
            PHONE_NUMBER_CONVERTER.convertToDatabaseColumn(userJpa.getPhoneNumber()),
            userJpa.getCreatedAt());
    if (insertedRows == 0) {
      log.debug("User with id {} already exists, skipping insertion.", user.getId().value());
    }
  }

  @Override
  public void update(@NonNull User user) {
    Optional<UserJpa> existing = userSpringRepository.findById(user.getId().value());
    if (existing.isPresent()) {
      UserJpa userJpa = existing.get();
      UserJpa updated = jpaMapper.toUserJpa(user);
      userJpa.setFirstname(updated.getFirstname());
      userJpa.setLastname(updated.getLastname());
      userJpa.setEmail(updated.getEmail());
      userJpa.setPhoneNumber(updated.getPhoneNumber());
      userSpringRepository.save(userJpa);
      log.debug("User with id {} updated.", user.getId().value());
    } else {
      log.debug("User with id {} not found, skipping update.", user.getId().value());
    }
  }

  @Override
  public void deleteById(@NonNull UserId userId) {
    userSpringRepository.deleteById(userId.value());
    log.debug("User with id {} deleted.", userId.value());
  }
}
