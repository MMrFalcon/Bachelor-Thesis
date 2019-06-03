package com.falcon.forum.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTestIT {

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
    }

    @Test
    public void user() {
    }

    @WithMockUser(username = "Falcon", password = "password", authorities = {"READ_AUTHORITY", "WRITE_PRIVILEGE"})
    @Test
    public void indexWithLoggedInUser() throws Exception {
        when(userService.getUserByName("Falcon")).thenReturn(Mapper.userToDTO(userEntity));
        mockMvc.perform(get("/index"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/panel/1"));
    }

    @Test
    public void index() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
}