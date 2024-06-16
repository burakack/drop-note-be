package burak.controller;

import burak.dto.NoteDto;
import burak.model.AppUser;
import burak.model.Note;
import burak.service.NoteService;
import burak.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private UserService userService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testNewNote() throws Exception {
        NoteDto noteDto = new NoteDto();
        noteDto.setTitle("Test Note");
        noteDto.setContent("This is a test note.");

        AppUser user = new AppUser();
        user.setUsername("testuser");

        Note note = new Note();
        note.setTitle("Test Note");
        note.setContent("This is a test note.");
        note.setUser(user);

        when(userService.search("testuser")).thenReturn(user);
        when(modelMapper.map(any(NoteDto.class), any(Class.class))).thenReturn(note);
        when(noteService.create(any(Note.class))).thenReturn(note);

        mockMvc.perform(post("/notes/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Note\", \"content\": \"This is a test note.\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Note"))
                .andExpect(jsonPath("$.content").value("This is a test note."));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testDeleteNote() throws Exception {
        Note note = new Note();
        AppUser user = new AppUser();
        user.setUsername("testuser");
        note.setUser(user);

        when(noteService.getNoteById(1L)).thenReturn(note);

        mockMvc.perform(delete("/notes/delete-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testUpdateNote() throws Exception {
        Note note = new Note();
        AppUser user = new AppUser();
        user.setUsername("testuser");
        note.setUser(user);
        note.setId(1L);
        note.setContent("Old Content"); // Eski content

        NoteDto noteDto = new NoteDto();
        noteDto.setId(1L);
        noteDto.setContent("Updated Content");

        Note updatedNote = new Note();
        updatedNote.setId(1L);
        updatedNote.setContent("Updated Content"); // Yeni content
        updatedNote.setUser(user);

        when(noteService.getNoteById(1L)).thenReturn(note);
        when(noteService.update(any(Note.class))).thenReturn(updatedNote);

        mockMvc.perform(put("/notes/update-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"title\": \"Updated Title\", \"content\": \"Updated Content\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testLikeNote() throws Exception {
        mockMvc.perform(post("/notes/like-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testDislikeNote() throws Exception {
        mockMvc.perform(post("/notes/dislike-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testRemoveLike() throws Exception {
        mockMvc.perform(post("/notes/remove-like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testRemoveDislike() throws Exception {
        mockMvc.perform(post("/notes/remove-dislike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testGetNotesByTitle() throws Exception {
        Note note = new Note();
        note.setTitle("Test Note");

        Set<Note> notes = Collections.singleton(note);

        when(noteService.getNotesByTitle("Test Note")).thenReturn(notes);

        mockMvc.perform(post("/notes/get-notes-by-title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Note\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Note"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testGetMyNotes() throws Exception {
        Note note = new Note();
        AppUser user = new AppUser();
        user.setUsername("testuser");
        note.setUser(user);

        Set<Note> notes = Collections.singleton(note);

        when(noteService.getMyNotes("testuser")).thenReturn(notes);

        mockMvc.perform(get("/notes/get-my-notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.username").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testGetUserNotes() throws Exception {
        Note note = new Note();
        AppUser user = new AppUser();
        user.setUsername("anotheruser");
        note.setUser(user);

        Set<Note> notes = Collections.singleton(note);

        when(noteService.getUserNotes("anotheruser")).thenReturn(notes);

        mockMvc.perform(get("/notes/get-user-notes/anotheruser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.username").value("anotheruser"));
    }
}
