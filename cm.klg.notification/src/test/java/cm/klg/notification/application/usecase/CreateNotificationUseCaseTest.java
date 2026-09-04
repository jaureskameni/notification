package cm.klg.notification.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import cm.klg.notification.application.outbound.NotificationRepository;
import cm.klg.notification.domaine.notification.Notification;
import cm.klg.notification.domaine.notification.NotificationReferenceId;
import cm.klg.notification.domaine.notification.NotificationType;
import cm.klg.notification.domaine.notification.ReferenceType;
import cm.klg.notification.domaine.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateNotificationUseCaseTest {

  @Mock private NotificationRepository notificationRepository;
  @InjectMocks private CreateNotificationUseCase createNotificationUseCase;

  @Test
  void shouldCreateNotificationTest() {
    UUID recipientId = UUID.randomUUID();
    UUID actorId = UUID.randomUUID();
    UUID serviceProviderId = UUID.randomUUID();

    createNotificationUseCase.execute(
        new CreateNotificationUseCase.CreateNotificationCommand(
            UserId.from(recipientId),
            UserId.from(actorId),
            NotificationType.SERVICE_PROVIDER_APPROVED,
            ReferenceType.SERVICE_PROVIDER,
            NotificationReferenceId.from(serviceProviderId)));

    ArgumentCaptor<Notification> notificationCaptor = ArgumentCaptor.forClass(Notification.class);
    verify(notificationRepository).insert(notificationCaptor.capture());
    assertThat(notificationCaptor.getValue())
        .satisfies(
            notification -> {
              assertThat(notification.getRecipientId().value()).isEqualTo(recipientId);
              assertThat(notification.getActorId().value()).isEqualTo(actorId);
              assertThat(notification.getNotificationType())
                  .isEqualTo(NotificationType.SERVICE_PROVIDER_APPROVED);
              assertThat(notification.getReferenceType()).isEqualTo(ReferenceType.SERVICE_PROVIDER);
              assertThat(notification.getReferenceId().value()).isEqualTo(serviceProviderId);
            });
  }
}
