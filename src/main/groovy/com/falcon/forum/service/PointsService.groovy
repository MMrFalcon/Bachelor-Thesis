package com.falcon.forum.service

import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO

interface PointsService {
    void addPointsToUser(Long points, UserDTO userDTO)
    void addPlusPointsToUser(Long points, UserDTO userDTO)
    void addMinusPointsToUser(Long points, UserDTO userDTO)
    void addPointsToPost(Long points, PostDTO postDTO, String voteAuthorName)
    void subtractPointsFromUser(Long points, UserDTO userDTO)
    void subtractPointsFromPost(Long points, PostDTO postDTO, String voteAuthorName)
    void addPointsToAnswer(Long points, CommentsDTO commentsDTO, String voteAuthorName)
    void subtractPointsFromAnswer(Long points, CommentsDTO commentsDTO, String voteAuthorName)
    void markAnswerAsCorrect(CommentsDTO commentsDTO)
}