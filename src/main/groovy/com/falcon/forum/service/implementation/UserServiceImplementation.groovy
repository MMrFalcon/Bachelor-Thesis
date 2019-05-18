package com.falcon.forum.service.implementation

import com.falcon.forum.exception.DuplicateEmailException
import com.falcon.forum.exception.DuplicateUsernameException
import com.falcon.forum.exception.UserNotFoundException
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.User
import com.falcon.forum.repository.UserRepository
import com.falcon.forum.service.UserService
import groovy.util.logging.Slf4j
import org.postgresql.util.PSQLState
import org.springframework.stereotype.Service

@Slf4j
@Service
class UserServiceImplementation extends BaseServiceImplementation<User, Long, UserRepository> implements UserService {

    private final UserRepository userRepository

    UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository
        super.setRepository(userRepository)
    }

    @Override
    UserDTO createUser(UserDTO userDTO) {
        if (userDTO == null)
            throw new NullPointerException("You are trying to save an empty Object!")
        else {

            try {
                UserDTO user = getUserByName(userDTO.getUsername())
                throw new DuplicateUsernameException("User ${user.username} already exist", PSQLState.DATA_ERROR)
            } catch (UserNotFoundException exception) {
                log.info("Adding usernem: ${userDTO.username} to new user")
            }
            try {
                UserDTO user = getUserByEmail(userDTO.getEmail())
                throw new DuplicateEmailException("Email already exist", PSQLState.DATA_ERROR)
            } catch (UserNotFoundException exception) {
                log.info("Adding email ${userDTO.email} to new user")
            }

            User user = save(Mapper.dtoToUser(userDTO))
            log.info("New user with id ${userRepository.getOne(user.getId())} was created")
            return Mapper.userToDTO(user)
        }
    }

    @Override
    UserDTO getUserByName(String name) {
        User userEntity = userRepository.findByUsername(name)
        try {
            UserDTO user = Mapper.userToDTO(userEntity)
            return user
        } catch (IllegalArgumentException exception) {
            throw new UserNotFoundException("Cannot find user with name ${name}")
        }
    }

    @Override
    UserDTO getUserByEmail(String email) {
        try {
            User userEntity = userRepository.findByEmail(email)
            return Mapper.userToDTO(userEntity)
        } catch (IllegalArgumentException exception) {
            throw new UserNotFoundException("Cannot find user with email ${email}")
        }
    }

}
