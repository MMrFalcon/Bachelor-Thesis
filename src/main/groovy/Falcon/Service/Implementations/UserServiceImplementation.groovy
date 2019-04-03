package Falcon.Service.Implementations

import Falcon.Model.UserDTO
import Falcon.Persist.User
import Falcon.Repository.UserRepository
import Falcon.Service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImplementation extends BaseServiceImplementation<User, Long, UserRepository> implements UserService {

    @Autowired
    private UserRepository userRepository

    @Override
    UserRepository getRepository() { userRepository }

    @Override
    UserDTO createUser(UserDTO userDTO) {
        if (userDTO == null)
            throw new NullPointerException("You are trying to save an empty Object!")
        else {
            User user = save(Mapper.dtoToUser(userDTO))
            return Mapper.userToDTO(user)
        }
    }

    @Override
    UserDTO getUserByName(String name) {
        User userEntity = userRepository.findByUsername(name)
        return Mapper.userToDTO(userEntity)
    }

    @Override
    void updateUserPoints(String username, Long points) {
        if (username == null)
            throw new NullPointerException("Username doesn't exist")
        if (points == null)
            throw new NullPointerException("Empty object POINTS")

        try {
            User userEntity =  userRepository.findByUsername(username)
            userEntity.setPoints(userEntity.getPoints() + points)
            saveAndFlush(userEntity)
        }catch(Exception ex) {
            println(ex)
        }
    }
}
