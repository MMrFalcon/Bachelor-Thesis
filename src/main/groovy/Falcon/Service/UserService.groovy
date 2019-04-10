package Falcon.Service

import Falcon.Model.UserDTO
import Falcon.Persist.User
import Falcon.Repository.UserRepository



interface UserService extends BaseService<User, Long, UserRepository> {
    UserDTO createUser(UserDTO userDTO)
    UserDTO getUserByName(String name)
    void updateUserPoints(String username, Long points)
//    UserDTO setActivation(Long id) FIXME
//    UserDTO updateUser(Long id) FIXME
}