package cm.klg.notification.domaine.user;

public record Lastname(String value) {
  public static Lastname from(String value) {
    return new Lastname(value);
  }
}
