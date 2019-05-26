package com.falcon.forum.service

import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.User
import com.falcon.forum.repository.UserRepository


interface UserService extends BaseService<User, Long, UserRepository> {
    UserDTO createUser(UserDTO userDTO)
    UserDTO getUserByName(String name)
    UserDTO getUserByEmail(String email)
    Long getCountOfCorrectAnswers(UserDTO userDTO)
    Long getAnswersPoints(UserDTO userDTO)
    Long getPostsPoints(UserDTO userDTO)
    Long getNumberOfAddedPosts(UserDTO userDTO)
    Long getNumberOfAddedAnswers(UserDTO userDTO)
    List<CommentsDTO> getCommentsDto(UserDTO userDTO)
    List<PostDTO> getPostsDto(UserDTO userDTO)

}