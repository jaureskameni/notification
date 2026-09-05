package cm.klg.notification.domaine.user;

import org.jspecify.annotations.Nullable;

public record UserProfile(
    @Nullable Firstname firstname,
    Lastname lastname,
    PhoneNumber phoneNumber,
    @Nullable Email email) {}
