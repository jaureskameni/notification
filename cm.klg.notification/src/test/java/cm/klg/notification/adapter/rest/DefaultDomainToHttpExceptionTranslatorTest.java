package cm.klg.notification.adapter.rest;

import static org.assertj.core.api.Assertions.assertThat;

import cm.klg.common.base.exception.ForbiddenException;
import cm.klg.common.base.exception.HttpErrorException;
import cm.klg.common.base.exception.InternalException;
import cm.klg.common.base.exception.ResourceNotFoundException;
import cm.klg.notification.domaine.notification.NotificationNotFoundException;
import cm.klg.notification.domaine.notification.UserNotAuthorizedToOpenNotificationException;
import cm.klg.notification.domaine.user.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultDomainToHttpExceptionTranslatorTest {

  private DefaultDomainToHttpExceptionTranslator translator;

  @BeforeEach
  void setUp() {
    translator = new DefaultDomainToHttpExceptionTranslator();
  }

  @Test
  void shouldTranslateUserNotFoundExceptionToResourceNotFoundTest() {
    UserNotFoundException exception = new UserNotFoundException();

    HttpErrorException httpException = translator.translate(exception);

    assertThat(httpException).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void shouldTranslateNotificationNotFoundExceptionToResourceNotFoundTest() {
    NotificationNotFoundException exception = new NotificationNotFoundException();

    HttpErrorException httpException = translator.translate(exception);

    assertThat(httpException).isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void shouldTranslateUserNotAuthorizedExceptionToForbiddenTest() {
    UserNotAuthorizedToOpenNotificationException exception =
        new UserNotAuthorizedToOpenNotificationException();

    HttpErrorException httpException = translator.translate(exception);

    assertThat(httpException).isInstanceOf(ForbiddenException.class);
  }

  @Test
  void shouldTranslateUnknownExceptionToInternalExceptionTest() {
    RuntimeException exception = new RuntimeException("Unknown error");

    HttpErrorException httpException = translator.translate(exception);

    assertThat(httpException).isInstanceOf(InternalException.class);
    assertThat(httpException.getMessage()).isEqualTo("Unknown error");
  }

  @Test
  void shouldUseMissingErrorCodeWhenMessageIsNullTest() {
    RuntimeException exception = new RuntimeException((String) null);

    HttpErrorException httpException = translator.translate(exception);

    assertThat(httpException).isInstanceOf(InternalException.class);
    assertThat(httpException.getMessage()).isEqualTo("missing error code");
  }
}
