package burak.repository;

import burak.model.AppUser;
import burak.model.Note;
import burak.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

    Notification findByNoteAndUser(Note note, AppUser user);

    List<Notification> findAllByUser(AppUser user);
}
