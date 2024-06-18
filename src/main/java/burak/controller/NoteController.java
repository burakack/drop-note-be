package burak.controller;

import burak.dto.NoteDto;
import burak.model.AppUser;
import burak.model.Note;
import burak.service.NoteService;
import burak.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping("/notes")
@Api(tags = "notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("new-note")
    @ApiOperation(value = "${NoteController.addNote}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public Note newNote(
            @ApiParam("NoteDto") @RequestBody NoteDto noteDto, HttpServletRequest req) {

        AppUser user = userService.search(req.getRemoteUser());
        noteDto.setUser(user);

        Note note = modelMapper.map(noteDto, Note.class);

        return noteService.create(note);
    }

    @DeleteMapping("delete-note")
    @ApiOperation(value = "${NoteController.deleteNote}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteNote(
            @ApiParam("NoteDto") @RequestBody NoteDto noteDto, HttpServletRequest req) {

        Note note = noteService.getNoteById(noteDto.getId());
        if (!note.getUser().getUsername().equals(req.getRemoteUser())) {
            throw new RuntimeException("You are not authorized to delete this note");
        } else {
            noteService.deleteNoteById(noteDto.getId());
        }

    }

    @PutMapping("update-note")
    @ApiOperation(value = "${NoteController.updateNote}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public Note updateNote(
            @ApiParam("NoteDto") @RequestBody NoteDto noteDto, HttpServletRequest req) {
        // return noteService.update(modelMapper.map(noteDto, Note.class));

        Note note = noteService.getNoteById(noteDto.getId());
        if (!note.getUser().getUsername().equals(req.getRemoteUser())) {
            throw new RuntimeException("You are not authorized to delete this note");
        } else {
            return noteService.update(modelMapper.map(noteDto, Note.class));
        }

    }

    @PostMapping("like-note")
    @ApiOperation(value = "${NoteController.likeNote}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void likeNote(
            @ApiParam("NoteDto") @RequestBody NoteDto noteDto, HttpServletRequest req) {
        noteService.likeNoteById(noteDto.getId(), req.getRemoteUser());
    }

    @PostMapping("dislike-note")
    @ApiOperation(value = "${NoteController.dislikeNote}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void dislikeNote(
            @ApiParam("NoteDto") @RequestBody NoteDto noteDto, HttpServletRequest req) {
        noteService.dislikeNoteById(noteDto.getId(), req.getRemoteUser());
    }

    @PostMapping("remove-like")
    @ApiOperation(value = "${NoteController.removeLike}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeLike(
            @ApiParam("NoteDto") @RequestBody NoteDto noteDto, HttpServletRequest req) {
        noteService.removeLikeById(noteDto.getId(), req.getRemoteUser());
    }

    @PostMapping("remove-dislike")
    @ApiOperation(value = "${NoteController.removeDislike}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeDislike(
            @ApiParam("NoteDto") @RequestBody NoteDto noteDto, HttpServletRequest req) {
        noteService.removeDislikeById(noteDto.getId(), req.getRemoteUser());
    }

    @PostMapping("get-notes-by-title")
    @ApiOperation(value = "${NoteController.getNoteByTitle}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    public Set<Note> getNotesByTitle(
            @ApiParam("NoteDto") @RequestBody NoteDto noteDto) {
        return noteService.getNotesByTitle(noteDto.getTitle());
    }

    @GetMapping("get-my-notes")
    @ApiOperation(value = "${NoteController.getMyNotes}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public Set<Note> getMyNotes(HttpServletRequest req) {
        return noteService.getMyNotes(req.getRemoteUser());
    }

    @GetMapping("get-user-notes/{username}")
    @ApiOperation(value = "${NoteController.getUserNotes}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public Set<Note> getUserNotes(@ApiParam("Username") @PathVariable String username) {
        return noteService.getUserNotes(username);
    }

    @GetMapping("test")
    public String test() {
        return "test işi degisiklik";
    }




}
