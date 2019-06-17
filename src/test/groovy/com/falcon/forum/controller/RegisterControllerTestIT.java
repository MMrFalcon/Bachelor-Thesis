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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RegisterController.class)
public class RegisterControllerTestIT {

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

    @WithMockUser()
    @Test
    public void getRegisterPage() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registerPage"));
    }

    @WithMockUser()
    @Test
    public void processRegister() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "Falcon");
        params.add("email", "mail@mail.com");
        params.add("password", "secret123");
        params.add("passwordConfirmation", "secret123");

        when(userService.createUser(any())).thenReturn(userDTO);
        mockMvc.perform(post("/registration")
                .with(csrf())
                .params(params)
        )
                .andExpect(redirectedUrl("/registerSuccess"))
                .andExpect(status().is3xxRedirection());
        verify(userService, times(1)).createUser(any());
    }

    @WithMockUser()
    @Test
    public void processRegisterWithBindingError() throws Exception {
        UserDTO userDTO = Mapper.userToDTO(userEntity);

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "Falcon");
        params.add("email", "mail@mail.com");
        params.add("password", "secret");

        when(userService.createUser(any())).thenReturn(userDTO);
        mockMvc.perform(post("/registration")
                .with(csrf())
                .params(params)
        )
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasErrors("userDTO"))
                .andExpect(view().name("user/registerPage"));
    }

    @WithMockUser()
    @Test
    public void registerSuccess() throws Exception {
        mockMvc.perform(get("/registerSuccess"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registerSuccess"));
    }
}