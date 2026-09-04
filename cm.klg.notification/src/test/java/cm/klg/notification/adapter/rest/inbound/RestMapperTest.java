package cm.klg.notification.adapter.rest.inbound;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cm.klg.generated.notifications.adapter.rest.inbound.dto.NotificationStatusDTO;
import cm.klg.notification.application.usecase.GetMyNotificationsUseCase;
import cm.klg.notification.application.usecase.GetMyUnreadNotificationCountUseCase;
import cm.klg.notification.domaine.notification.NotificationId;
import cm.klg.notification.domaine.notification.NotificationStatus;
import cm.klg.notification.domaine.user.UserId;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class RestMapperTest {

  private RestMapper restMapper;

  @BeforeEach
  void setUp() {
    restMapper = Mappers.getMapper(RestMapper.class);
  }

  @Test
  void shouldMapToMyNotificationQueryTest() {
    UserId currentUserId = UserId.from(UUID.randomUUID());

    GetMyNotificationsUseCase.Query query =
        restMapper.toMyNotificationquery(currentUserId, null, 20, 5);

    assertThat(query.recipientId()).isEqualTo(currentUserId);
    assertThat(query.status()).isNull();
    assertThat(query.limit()).isEqualTo(20);
    assertThat(query.page()).isEqualTo(5);
  }

  @Test
  void shouldSetDefaultLimitTo10Test() {
    UserId currentUserId = UserId.from(UUID.randomUUID());

    GetMyNotificationsUseCase.Query query =
        restMapper.toMyNotificationquery(currentUserId, null, null, 0);

    assertThat(query.limit()).isEqualTo(10);
  }

  @Test
  void shouldCapLimitTo100Test() {
    UserId currentUserId = UserId.from(UUID.randomUUID());

    GetMyNotificationsUseCase.Query query =
        restMapper.toMyNotificationquery(currentUserId, null, 200, 0);

    assertThat(query.limit()).isEqualTo(100);
  }

  @Test
  void shouldThrowExceptionWhenLimitIsNegativeTest() {
    UserId currentUserId = UserId.from(UUID.randomUUID());

    assertThatThrownBy(() -> restMapper.toMyNotificationquery(currentUserId, null, -1, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Limit cannot be less than 0");
  }

  @Test
  void shouldSetDefaultPageTo0Test() {
    UserId currentUserId = UserId.from(UUID.randomUUID());

    GetMyNotificationsUseCase.Query query =
        restMapper.toMyNotificationquery(currentUserId, null, 10, null);

    assertThat(query.page()).isZero();
  }

  @Test
  void shouldThrowExceptionWhenPageIsNegativeTest() {
    UserId currentUserId = UserId.from(UUID.randomUUID());

    assertThatThrownBy(() -> restMapper.toMyNotificationquery(currentUserId, null, 10, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Page cannot be less than 0");
  }

  @Test
  void shouldConvertStatusDTOToNotificationStatusTest() {
    UserId currentUserId = UserId.from(UUID.randomUUID());

    GetMyNotificationsUseCase.Query query =
        restMapper.toMyNotificationquery(currentUserId, NotificationStatusDTO.UNREAD, 10, 0);

    assertThat(query.status()).isEqualTo(NotificationStatus.UNREAD);
  }

  @Test
  void shouldMapToCountNotificationCommandTest() {
    UserId currentUserId = UserId.from(UUID.randomUUID());

    GetMyUnreadNotificationCountUseCase.Command command =
        restMapper.toCountNotificationCommand(currentUserId, NotificationStatusDTO.READ);

    assertThat(command.recipientId()).isEqualTo(currentUserId);
    assertThat(command.status()).isEqualTo(NotificationStatus.READ);
  }

  @Test
  void shouldMapToOpenMyNotificationCommandTest() {
    UserId currentUserId = UserId.from(UUID.randomUUID());
    NotificationId notificationId = NotificationId.generate();

    cm.klg.notification.application.usecase.OpenMyNotificationUseCase.Command command =
        restMapper.toOpenMyNotificationCommand(currentUserId, notificationId);

    assertThat(command.recipientId()).isEqualTo(currentUserId);
    assertThat(command.notificationId()).isEqualTo(notificationId);
  }
}
