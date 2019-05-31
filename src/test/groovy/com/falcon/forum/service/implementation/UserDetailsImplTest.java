package com.falcon.forum.service.implementation;

import com.falcon.forum.persist.Authorities;
import com.falcon.forum.persist.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserDetailsImplTest {

    private UserDetailsImpl userDetails;
    private User user;

    @Before
    public void setUp() throws Exception {
        user =  new User();
        user.setId(1L);
        user.setUsername("Falcon");
        user.setPassword("Secret");
        user.setActive(true);

        userDetails = new UserDetailsImpl(user);
    }

    @Test
    public void getAuthorities() {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Authorities.READ_AUTHORITY.toString()));
        authorities.add(new SimpleGrantedAuthority(Authorities.WRITE_AUTHORITY.toString()));

        Object[] authoritiesFromMethod = userDetails.getAuthorities().toArray();

        assertEquals(authoritiesFromMethod[0], authorities.get(0));
        assertEquals(authoritiesFromMethod[1], authorities.get(1));
    }

    @Test
    public void getPassword() {
        String password = userDetails.getPassword();
        assertEquals(password, user.getPassword());
    }

    @Test
    public void getUsername() {
        String username = userDetails.getUsername();
        assertEquals(username, user.getUsername());
    }

    @Test
    public void isAccountNonExpired() {
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    public void isAccountNonLocked() {
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    public void isCredentialsNonExpired() {
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    public void isEnabled() {
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void getUser() {
        assertEquals(user, userDetails.getUser());
    }
}