package com.falcon.forum.service.implementation

import com.falcon.forum.model.UserDTO
import com.falcon.forum.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService

    @Autowired
    PasswordEncoder passwordEncoder

    UserDetailsServiceImpl() { super() }


    @Override
    UserDetails loadUserByUsername(String nameOfUser) throws UsernameNotFoundException {
        UserDTO user = userService.getUserByName(nameOfUser)
        if(!user) { throw new UsernameNotFoundException(nameOfUser)}

        user.setPassword(passwordEncoder.encode(user.getPassword()))

        return new UserDetailsImpl(Mapper.dtoToUser(user))
    }
}
