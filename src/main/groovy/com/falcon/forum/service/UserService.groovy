package com.falcon.forum.service


import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.User
import com.falcon.forum.repository.UserRepository


interface UserService extends BaseService<User, Long, UserRepository> {
    UserDTO createUser(UserDTO userDTO)
    UserDTO getUserByName(String name)
    UserDTO getUserByEmail(String email)
}