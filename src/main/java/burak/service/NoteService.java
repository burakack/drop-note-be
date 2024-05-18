package burak.service;

import burak.model.AppUser;
import burak.model.Note;
import burak.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {


    private final NoteRepository noteRepository;
    private final UserService userService;

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
        //eger kullanici daha once dislike ettiyse, dislike'i kaldir ve like'i ekle
        if (note.getDislikedBy().contains(userService.getUserInformationByUsername(userName))) {
            note.getDislikedBy().remove(userService.getUserInformationByUsername(userName));
            note.getLikedBy().add(userService.getUserInformationByUsername(userName));
            noteRepository.save(note);
            return;
        } else if (note.getLikedBy().contains(userService.getUserInformationByUsername(userName))) {
            return;
        } else {
            note.getLikedBy().add(userService.getUserInformationByUsername(userName));
            noteRepository.save(note);
        }
        noteRepository.save(note);
    }

    public void dislikeNoteById(Long noteId, String userName) {

        Note note = getNoteById(noteId);
        //eger kullanici daha once like ettiyse, like'i kaldir ve dislike'i ekle
        if (note.getLikedBy().contains(userService.getUserInformationByUsername(userName))) {
            note.getLikedBy().remove(userService.getUserInformationByUsername(userName));
            note.getDislikedBy().add(userService.getUserInformationByUsername(userName));
            noteRepository.save(note);
            return;
        } else if (note.getDislikedBy().contains(userService.getUserInformationByUsername(userName))) {
            return;
        } else {
            note.getDislikedBy().add(userService.getUserInformationByUsername(userName));
            noteRepository.save(note);
        }

        noteRepository.save(note);
    }

    public void removeLikeById(Long id, String userName) {
        Note note = getNoteById(id);
        if (note.getLikedBy().contains(userService.getUserInformationByUsername(userName))) {
            note.getLikedBy().remove(userService.getUserInformationByUsername(userName));
            noteRepository.save(note);
        }
    }

    public void removeDislikeById(Long id, String userName) {
        Note note = getNoteById(id);
        if (note.getDislikedBy().contains(userService.getUserInformationByUsername(userName))) {
            note.getDislikedBy().remove(userService.getUserInformationByUsername(userName));
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
                .collect(Collectors.toSet());
    }

    public Set<Note> getMyNotes(String userName) {
        return noteRepository.findAllByUser(userService.getUserInformationByUsername(userName));


    }


}
