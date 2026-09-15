package cm.klg.notification.application.usecase;

import cm.klg.common.base.domain.CreatedAt;
import cm.klg.notification.application.outbound.UserRepository;
import cm.klg.notification.domaine.user.Email;
import cm.klg.notification.domaine.user.Firstname;
import cm.klg.notification.domaine.user.Lastname;
import cm.klg.notification.domaine.user.PhoneNumber;
import cm.klg.notification.domaine.user.User;
import cm.klg.notification.domaine.user.UserId;
import cm.klg.notification.domaine.user.UserProfile;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class UpdateUserUseCase {
  private final UserRepository userRepository;

  public void execute(UpdateUserCommand command) {
    Firstname firstname = Firstname.from(command.profile().firstname());
    Email email = Email.from(command.profile().email());
    PhoneNumber phoneNumber = new PhoneNumber(command.countryCode(), command.phoneNumber());
    Lastname lastname = Lastname.from(command.profile().lastname());

    UserProfile userProfile = new UserProfile(firstname, lastname, phoneNumber, email);
    User updatedUser =
        User.reconstitute(
            UserId.from(command.id()), userProfile, CreatedAt.from(command.updatedAt));

    userRepository.update(updatedUser);
  }

  public record UpdateUserCommand(
      UUID id,
      UserProfileCommand profile,
      String countryCode,
      String phoneNumber,
      LocalDateTime updatedAt) {
    public record UserProfileCommand(
        String lastname, @Nullable String firstname, @Nullable String email) {}
  }
}
