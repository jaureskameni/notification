package cm.klg.notification.domaine.notification;

import cm.klg.notification.domaine.user.UserId;

public record NotificationDetails(
    UserId recipientId,
    UserId actorId,
    NotificationType notificationType,
    ReferenceType referenceType,
    NotificationReferenceId referenceId,
    RejectionReason reason) {}
