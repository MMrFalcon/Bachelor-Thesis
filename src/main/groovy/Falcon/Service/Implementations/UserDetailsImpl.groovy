package Falcon.Service.Implementations


import Falcon.Persist.Authorities
import Falcon.Persist.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl implements UserDetails {

    private final User user

    UserDetailsImpl(User user) { this.user = user}

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>()
            authorities.add(new SimpleGrantedAuthority(Authorities.READ_AUTHORITY.toString()))
            authorities.add(new SimpleGrantedAuthority(Authorities.WRITE_AUTHORITY.toString()))
        return authorities
    }

    @Override
    String getPassword() {
        user.getPassword()
    }

    @Override
    String getUsername() {
        user.getUsername()
    }

    @Override
    boolean isAccountNonExpired() {
        return true
    }

    @Override
    boolean isAccountNonLocked() {
        return true
    }

    @Override
    boolean isCredentialsNonExpired() {
        return true
    }

    @Override
    boolean isEnabled() {
       return user.isActive()
    }

    User getUser() { this.user }
}
