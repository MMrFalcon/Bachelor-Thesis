package com.falcon.forum.service.implementation

import com.falcon.forum.exception.CommentNotFoundException
import com.falcon.forum.exception.PostNotFoundException
import com.falcon.forum.exception.UserNotFoundException
import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.Comments
import com.falcon.forum.persist.Post
import com.falcon.forum.persist.User
import com.falcon.forum.repository.CommentsRepository
import com.falcon.forum.service.CommentsService
import com.falcon.forum.service.PostService
import com.falcon.forum.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service

@Slf4j
@Service
class CommentsServiceImplementation extends BaseServiceImplementation<Comments, Long, CommentsRepository>
        implements CommentsService {

    private final CommentsRepository commentsRepository
    private final UserService userService
    private final PostService postService
    CommentsServiceImplementation(CommentsRepository commentsRepository, UserService userService,
                                  PostService postService ) {
        this.commentsRepository = commentsRepository
        this.userService = userService
        this.postService = postService
        super.setRepository(commentsRepository)
    }

    @Override
    CommentsDTO createComment(CommentsDTO commentsDTO, UserDTO author, PostDTO post) {
        Comments commentsEntity =  Mapper.dtoToComments(commentsDTO)
        User user = userService.getOne(author.getId())
        Post postEntity = postService.getOne(post.getId())

        if (user == null) {
            throw new UserNotFoundException("Cannot find user")
        }

        if (postEntity == null) {
            throw new PostNotFoundException("Cannot find post")
        }

        log.info("User ${user.getUsername()} starts creation of new comment for post with id ${postEntity.getId()}")
        commentsEntity.setUser(user)
        commentsEntity.setPost(postEntity)
        Comments savedComment = save(commentsEntity)
        log.info("Comment with id ${savedComment.getId()} successfully saved")
        CommentsDTO commentsTransferObj = Mapper.commentsToDto(savedComment)
        return commentsTransferObj
    }

    @Override
    CommentsDTO updateComment(CommentsDTO commentsDTO, Long commentId) {

        log.info("Searching for comment with id ${commentId}")
        Comments commentEntity = getOne(commentId)

        if (commentEntity == null) {
            throw new CommentNotFoundException("Cannot find comment with id ${commentId}")
        }
        log.info("Updating comment with id ${commentId}")
        commentEntity.setCommentMessage(commentsDTO.getCommentMessage())
        commentEntity.setUpdatedDate(new Date())
        Comments savedComment = save(commentEntity)
        log.info("Comment with id ${savedComment.getId()} successfully updated")
        return Mapper.commentsToDto(savedComment)
    }

    @Override
    List<Comments> getComments(PostDTO postDTO) {
        Post post = postService.getOne(postDTO.getId())
        def comments  = [] as List<Comments>
        post.getComments().each {
            Comments comment ->
                comments.add(comment)
        }

        def sortedComments = comments.sort()

        return sortedComments
    }

    @Override
    CommentsDTO getCommentDtoById(Long commentId) {
        log.info("Searching for comment with id ${commentId}")
        Comments comment = getOne(commentId)
        return Mapper.commentsToDto(comment)
    }

}
