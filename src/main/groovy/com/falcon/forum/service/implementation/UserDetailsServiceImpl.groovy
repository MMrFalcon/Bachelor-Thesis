package com.falcon.forum.service.implementation

import com.falcon.forum.model.UserDTO
import com.falcon.forum.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Slf4j
@Service
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService

    @Autowired
    UserDetailsServiceImpl(UserService userService) {
        this.userService = userService
        super
    }


    @Override
    UserDetails loadUserByUsername(String nameOfUser) throws UsernameNotFoundException {

        UserDTO user = userService.getUserByName(nameOfUser)
        if(!user) { throw new UsernameNotFoundException("Cannot find user with name ${nameOfUser}")}

        return new UserDetailsImpl(Mapper.dtoToUser(user))
    }
}
