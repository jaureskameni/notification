package cm.klg.notification.domaine.user;

import cm.klg.common.base.domain.CreatedAt;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

@Getter
public class User {
  private final UserId id;
  private final IdentityId identityId;
  @Nullable private final Firstname firstname;
  private final Lastname lastname;
  private final PhoneNumber phoneNumber;
  @Nullable private final Email email;
  private final CreatedAt createdAt;

  public User(UserId id, IdentityId identityId, UserProfile userProfile, CreatedAt createdAt) {
    this.id = id;
    this.identityId = identityId;
    this.firstname = userProfile.firstname();
    this.lastname = userProfile.lastname();
    this.phoneNumber = userProfile.phoneNumber();
    this.email = userProfile.email();
    this.createdAt = createdAt;
  }

  public static User reconstitute(
      UserId id, IdentityId identityId, UserProfile userProfile, CreatedAt createdAt) {
    return new User(id, identityId, userProfile, createdAt);
  }
}
