package burak.controller;

import burak.dto.UserDataDTO;
import burak.dto.UserResponseDTO;
import burak.dto.UserUpdateDto;
import burak.model.AppUser;
import burak.model.AppUserRole;
import burak.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    public void testLogin() throws Exception {
        UserDataDTO user = new UserDataDTO();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userService.signin("testuser", "password")).thenReturn("jwt-token");

        mockMvc.perform(post("/users/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    public void testSignup() throws Exception {
        UserDataDTO user = new UserDataDTO();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setAppUserRoles(Collections.singletonList(AppUserRole.ROLE_USER));

        AppUser appUser = new AppUser();
        appUser.setUsername("testuser");
        appUser.setPassword("password");
        appUser.setAppUserRoles(Collections.singletonList(AppUserRole.ROLE_USER));

        when(modelMapper.map(any(UserDataDTO.class), any(Class.class))).thenReturn(appUser);
        when(userService.signup(appUser)).thenReturn("jwt-token");

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testEditUserInformation() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setUsername("testuser");
        userUpdateDto.setAge(30);
        userUpdateDto.setDescription("Updated description");

        AppUser appUser = new AppUser();
        appUser.setUsername("testuser");
        appUser.setAge(30);
        appUser.setDescription("Updated description");

        when(userService.editUserInformation(any(UserUpdateDto.class))).thenReturn(appUser);

        mockMvc.perform(post("/users/edit-user-information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"age\": 30, \"description\": \"Updated description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.description").value("Updated description"));
    }


    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/testuser"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testSearchUser() throws Exception {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername("testuser");

        when(userService.search("testuser")).thenReturn(new AppUser());
        when(modelMapper.map(any(AppUser.class), any(Class.class))).thenReturn(userResponseDTO);

        mockMvc.perform(get("/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testWhoami() throws Exception {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername("testuser");

        when(userService.whoami(any(HttpServletRequest.class))).thenReturn(new AppUser());
        when(modelMapper.map(any(AppUser.class), any(Class.class))).thenReturn(userResponseDTO);

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }
}
