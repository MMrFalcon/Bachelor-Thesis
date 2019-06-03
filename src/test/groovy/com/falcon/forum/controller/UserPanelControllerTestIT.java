package com.falcon.forum.controller;

import com.falcon.forum.model.UserDTO;
import com.falcon.forum.persist.User;
import com.falcon.forum.service.UserService;
import com.falcon.forum.service.implementation.Mapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserPanelController.class)
public class UserPanelControllerTestIT {

    private User userEntity;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Before
    public void setUp() throws Exception {
        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("Falcon");
        userEntity.setActive(true);
        userEntity.setEmail("mail@mail.com");
        userEntity.setPoints(20L);
        userEntity.setMinusPoints(2L);
        userEntity.setPlusPoints(22L);
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void returnUserPanel() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);

        when(userService.getUserByName("Falcon")).thenReturn(userDTO);
        mockMvc.perform(get("/user/panel/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/userPanel"))
                .andExpect(model().attributeExists("points", "correctAnswers", "postsPoints", "answersPoints",
                        "negativePoints", "addedPosts", "addedAnswers", "posts", "answers"));
    }

    @Test
    public void returnUserPanelWith401() throws Exception {
        mockMvc.perform(get("user/panel/1"))
                .andExpect(status().isUnauthorized());
    }
}