package com.falcon.forum.service


import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.Comments

interface CommentsService {
    CommentsDTO createComment(CommentsDTO commentsDTO, UserDTO author, PostDTO post)
    CommentsDTO updateComment(CommentsDTO commentsDTO, Long commentId)
    List<Comments> getComments(PostDTO postDTO)
    CommentsDTO getCommentDtoById(Long commentId)
}