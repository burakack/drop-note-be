package burak.controller;

import burak.dto.FriendshipRequestDto;
import burak.model.AppUser;
import burak.model.Friendship;
import burak.service.FriendshipService;
import burak.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FriendshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendshipService friendshipService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testNewFriendshipRequest() throws Exception {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto();
        friendshipRequestDto.setOtherUserId(1L);

        doNothing().when(friendshipService).newFriendshipRequest(anyLong(), anyString());

        mockMvc.perform(post("/friendship/new-friendship-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"otherUserId\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testAcceptFriendshipRequest() throws Exception {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto();
        friendshipRequestDto.setOtherUserId(1L);

        doNothing().when(friendshipService).acceptFriendshipRequest(anyLong(), anyString());

        mockMvc.perform(post("/friendship/accept-friendship-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"otherUserId\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testDeleteFriendship() throws Exception {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto();
        friendshipRequestDto.setOtherUserId(1L);

        doNothing().when(friendshipService).deleteFriendship(anyLong(), anyString());

        mockMvc.perform(post("/friendship/remove-friendship")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"otherUserId\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testGetMyFriendships() throws Exception {
        Set<Friendship> friendships = new HashSet<>();
        Friendship friendship = new Friendship();
        friendships.add(friendship);

        when(friendshipService.getFriendships(anyString())).thenReturn(friendships);

        mockMvc.perform(get("/friendship/my-friendships"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testGetMyFriendRequests() throws Exception {
        Set<Friendship> friendRequests = new HashSet<>();
        Friendship friendship = new Friendship();
        friendRequests.add(friendship);

        when(friendshipService.getMyFriendRequests(anyString())).thenReturn(friendRequests);

        mockMvc.perform(get("/friendship/my-friend-requests"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testRemoveFriendRequest() throws Exception {
        FriendshipRequestDto friendshipRequestDto = new FriendshipRequestDto();
        friendshipRequestDto.setOtherUserId(1L);

        doNothing().when(friendshipService).removeFriendRequest(anyLong(), anyString());

        mockMvc.perform(post("/friendship/remove-friend-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"otherUserId\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testGetUserFriendships() throws Exception {
        Set<Friendship> friendships = new HashSet<>();
        Friendship friendship = new Friendship();
        friendships.add(friendship);

        when(friendshipService.getFriendships(anyString())).thenReturn(friendships);

        mockMvc.perform(get("/friendship/user-friendships/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }
}
