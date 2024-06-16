package burak.service;

import burak.model.AppUser;
import burak.model.Note;
import burak.model.Notification;
import burak.repository.NoteRepository;
import burak.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }

    public Note create(Note note) {
        return noteRepository.save(note);
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id).get();
    }

    public Note update(Note updateNote) {
        Note note = getNoteById(updateNote.getId());
        note.setContent(updateNote.getContent());
        note.setAnonymous(updateNote.isAnonymous());
        return noteRepository.save(note);
    }

    public void likeNoteById(Long noteId, String userName) {
        Note note = getNoteById(noteId);
        AppUser user = userService.getUserInformationByUsername(userName);

        if (note.getDislikedBy().contains(user)) {
            note.getDislikedBy().remove(user);
            note.getLikedBy().add(user);
        } else if (!note.getLikedBy().contains(user)) {
            note.getLikedBy().add(user);
        }

        noteRepository.save(note);
        createOrUpdateNotification(note, user, "liked");
    }

    public void dislikeNoteById(Long noteId, String userName) {
        Note note = getNoteById(noteId);
        AppUser user = userService.getUserInformationByUsername(userName);

        if (note.getLikedBy().contains(user)) {
            note.getLikedBy().remove(user);
            note.getDislikedBy().add(user);
        } else if (!note.getDislikedBy().contains(user)) {
            note.getDislikedBy().add(user);
        }

        noteRepository.save(note);
        createOrUpdateNotification(note, user, "disliked");
    }

    private void createOrUpdateNotification(Note note, AppUser user, String action) {
        Notification notification = notificationRepository.findByNoteAndUser(note, user);
        String content = "User " + user.getUsername() + " " + action + " your note. Total likes: " + note.getLikedBy().size();

        if (notification == null) {
            notification = new Notification();
            notification.setTitle("Your note was " + action);
            notification.setContent(content);
            notification.setRead(false);
            notification.setUser(note.getUser());
            notification.setNote(note);
            notificationService.createNotification(notification);
        } else {
            notification.setContent(content);
            notificationService.updateNotification(notification);
        }
    }

    public void removeLikeById(Long id, String userName) {
        Note note = getNoteById(id);
        AppUser user = userService.getUserInformationByUsername(userName);
        if (note.getLikedBy().contains(user)) {
            note.getLikedBy().remove(user);
            noteRepository.save(note);
        }
    }

    public void removeDislikeById(Long id, String userName) {
        Note note = getNoteById(id);
        AppUser user = userService.getUserInformationByUsername(userName);
        if (note.getDislikedBy().contains(user)) {
            note.getDislikedBy().remove(user);
            noteRepository.save(note);
        }
    }

    public Set<Note> getNotesByTitle(String title) {
        return noteRepository.findAllByTitleIgnoreCase(title)
                .stream()
                .peek(note -> {
                    if (note.isAnonymous()) {
                        note.setUser(null);
                    }
                })
                .sorted(Comparator.comparing(Note::getCreatedAt))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<Note> getMyNotes(String userName) {
        Set<Note> notes = noteRepository.findAllByUser(userService.getUserInformationByUsername(userName));
        notes.removeIf(Note::isAnonymous);
        return notes;
    }

    public Set<Note> getUserNotes(String userName) {
        Set<Note> notes = noteRepository.findAllByUser(userService.getUserInformationByUsername(userName));
        notes.removeIf(Note::isAnonymous);
        return notes;
    }
}
