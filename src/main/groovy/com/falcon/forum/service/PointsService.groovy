package com.falcon.forum.service

import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO

interface PointsService {
    void addPointsToUser(Long points, UserDTO userDTO)
    void addPointsToPost(Long points, PostDTO postDTO)
    void subtractPointsFromUser(Long points, UserDTO userDTO)
    void subtractPointsFromPost(Long points, PostDTO postDTO)
    void markAnswerAsCorrect(CommentsDTO commentsDTO)
}