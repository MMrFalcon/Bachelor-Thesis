package Falcon.Service

import Falcon.Model.CommentsDTO
import Falcon.Model.PostDTO
import Falcon.Model.UserDTO
import Falcon.Persist.Comments

interface CommentsService {
    CommentsDTO createComment(CommentsDTO commentsDTO, UserDTO author, PostDTO post)
    CommentsDTO updateComment(CommentsDTO commentsDTO, Long commentId)
    List<Comments> getComments(PostDTO postDTO)
    CommentsDTO getCommentDtoById(Long commentId)
}