package Falcon.Service.Implementations

import Falcon.Model.CommentsDTO
import Falcon.Persist.Comments
import Falcon.Repository.CommentsRepository
import Falcon.Service.CommentsService
import org.springframework.stereotype.Service

@Service
class CommentsServiceImplementation implements CommentsService {

    private final CommentsRepository commentsRepository

    CommentsServiceImplementation(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository
    }

    @Override
    CommentsDTO createComment(CommentsDTO commentsDTO) {
        Comments commentsEntity =  Mapper.dtoToComments(commentsDTO)

        return null
    }

    @Override
    CommentsDTO updateComment(CommentsDTO commentsDTO) {
        return null
    }
}
