package cm.klg.notification.domaine.user;

public record PhoneNumber(String countryCode, String number) {
  public static PhoneNumber from(String countryCode, String number) {
    return new PhoneNumber(countryCode, number);
  }

  public String value() {
    return countryCode + number;
  }
}
