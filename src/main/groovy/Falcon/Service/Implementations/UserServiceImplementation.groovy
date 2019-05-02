package Falcon.Service.Implementations

import Falcon.Exceptions.DuplicateEmailException
import Falcon.Exceptions.DuplicateUsernameException
import Falcon.Exceptions.UserNotFoundException
import Falcon.Model.UserDTO
import Falcon.Persist.User
import Falcon.Repository.UserRepository
import Falcon.Service.UserService
import org.postgresql.util.PSQLState
import org.springframework.stereotype.Service

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
                //fixme loger here
            }
            try {
                UserDTO user = getUserByEmail(userDTO.getEmail())
                throw new DuplicateEmailException("Email already exist", PSQLState.DATA_ERROR)
            } catch (UserNotFoundException exception) {
                //fixme loger here
            }

            User user = save(Mapper.dtoToUser(userDTO))
            println("New user ${userRepository.getOne(user.getId())} is created")
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

    @Override
    void updateUserPoints(String username, Long points) {
        if (username == null)
            throw new NullPointerException("Username doesn't exist")
        if (points == null)
            throw new NullPointerException("Empty object POINTS")

        try {
            User userEntity = userRepository.findByUsername(username)
            userEntity.setPoints(userEntity.getPoints() + points)
            println("Adding ${points} point/s to user ${userRepository.getOne(userEntity.getId())}")
            saveAndFlush(userEntity)
            println("User ${userRepository.getOne(userEntity.getId())} points after successfully flashing operation ${userEntity.getPoints()}")
        } catch (Exception ex) {
            println(ex)
        }
    }
}
