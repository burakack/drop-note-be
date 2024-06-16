package burak.repository;

import burak.model.AppUser;
import burak.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface NoteRepository extends JpaRepository<Note, Long> {

    Set<Note> findAllByTitleIgnoreCase(String title);


    Set<Note> findAllByUser(AppUser user);
}
