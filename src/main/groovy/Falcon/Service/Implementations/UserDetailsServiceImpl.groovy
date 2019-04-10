package Falcon.Service.Implementations

import Falcon.Model.UserDTO
import Falcon.Service.UserService
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
    Mapper mapper = new Mapper()

    @Override
    UserDetails loadUserByUsername(String nameOfUser) throws UsernameNotFoundException {
        UserDTO user = userService.getUserByName(nameOfUser)
        if(!user) { throw new UsernameNotFoundException(nameOfUser)}

        user.setPassword(passwordEncoder.encode(user.getPassword()))

        return new UserDetailsImpl(Mapper.dtoToUser(user))
    }
}