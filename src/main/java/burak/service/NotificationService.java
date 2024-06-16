package burak.service;

import burak.model.Notification;
import burak.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public void deleteNotificationById(Long id) {
        notificationRepository.deleteById(id);
    }

    public void deleteAllNotifications() {
        notificationRepository.deleteAll();
    }

    public void createNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).get();
    }

    public void updateNotification(Notification updateNotification) {
        Notification notification = getNotificationById(updateNotification.getId());
        notification.setContent(updateNotification.getContent());
        notificationRepository.save(notification);
    }

    public void markAsRead(Long id) {
        Notification notification = getNotificationById(id);
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void markAsUnread(Long id) {
        Notification notification = getNotificationById(id);
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findAllByUser(userService.getUserById(userId));
    }
}
