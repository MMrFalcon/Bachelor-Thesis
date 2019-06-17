package com.falcon.forum.service.implementation;

import com.falcon.forum.model.UserDTO;
import com.falcon.forum.persist.User;
import com.falcon.forum.service.UserService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl userDetailsService;
    private User user;
    private UserDTO userDTO;


    @Mock
    UserService userService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userDetailsService = new UserDetailsServiceImpl(userService);


        user =  new User();
        user.setId(1L);
        user.setUsername("Falcon");
        user.setPassword("Secret");
        user.setActive(true);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("Falcon");
        userDTO.setPassword("Secret");
        userDTO.setActive(true);
    }

    @Test
    public void loadUserByUsername() {
        when(userService.getUserByName(user.getUsername())).thenReturn(userDTO);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetailsService.loadUserByUsername(userDTO.getUsername());

        assertEquals(userDetailsImpl.getUser().getUsername(), user.getUsername());
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Test
    public void loadUserByUsernameDoesNotExist() {
        String badUserName = "Other";

        final String exceptionMessage = "Cannot find user with name " + badUserName;
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(exceptionMessage);

        when(userService.getUserByName(badUserName)).thenReturn(null);
        userDetailsService.loadUserByUsername(badUserName);

    }

}