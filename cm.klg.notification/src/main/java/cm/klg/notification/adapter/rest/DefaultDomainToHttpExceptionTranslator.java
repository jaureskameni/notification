package cm.klg.notification.adapter.rest;

import cm.klg.common.base.exception.ForbiddenException;
import cm.klg.common.base.exception.HttpErrorException;
import cm.klg.common.base.exception.InternalException;
import cm.klg.common.base.exception.ResourceNotFoundException;
import cm.klg.common.base.transaction.DomainToHttpExceptionTranslator;
import cm.klg.notification.domaine.notification.NotificationNotFoundException;
import cm.klg.notification.domaine.notification.UserNotAuthorizedToOpenNotificationException;
import cm.klg.notification.domaine.user.UserNotFoundException;
import java.util.Optional;

public record DefaultDomainToHttpExceptionTranslator() implements DomainToHttpExceptionTranslator {
  @Override
  public HttpErrorException translate(RuntimeException ex) {
    var message = Optional.ofNullable(ex.getMessage()).orElse("missing error code");
    return switch (ex) {
      case UserNotFoundException _, NotificationNotFoundException _ ->
          new ResourceNotFoundException(message);
      case UserNotAuthorizedToOpenNotificationException _ -> new ForbiddenException(message);
      default -> new InternalException(message, ex);
    };
  }
}
