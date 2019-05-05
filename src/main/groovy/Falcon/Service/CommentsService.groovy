package Falcon.Service

import Falcon.Model.CommentsDTO

interface CommentsService {
    CommentsDTO createComment(CommentsDTO commentsDTO)
    CommentsDTO updateComment(CommentsDTO commentsDTO)
}