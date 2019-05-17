package com.falcon.forum.service.implementation

import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.Comments
import com.falcon.forum.persist.Post
import com.falcon.forum.persist.User
import com.falcon.forum.service.CommentsService
import com.falcon.forum.service.PointsService
import com.falcon.forum.service.PostService
import com.falcon.forum.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service

@Slf4j
@Service
class PointsServiceImplementation implements PointsService {

    private final UserService userService
    private final CommentsService commentsService
    private final PostService postService

    PointsServiceImplementation(UserService userService, CommentsService commentsService, PostService postService) {
        this.userService = userService
        this.commentsService = commentsService
        this.postService = postService
    }

    @Override
    void addPointsToUser(Long points, UserDTO userDTO) {
        log.info("Searching for user with id ${userDTO.getId()} for points update operation...")
        User user = userService.getOne(userDTO.getId())
        Long actualUserPoints = user.getPoints()
        Long updatedPoints = actualUserPoints + points
        log.info("Actual user points: ${actualUserPoints} - points for add: ${points}")
        user.setPoints(updatedPoints)
        User updatedUser = userService.save(user)
        log.info("User points after saving operation: ${updatedUser.getPoints()}")
    }

    @Override
    void addPointsToPost(Long points, PostDTO postDTO) {
        log.info("Searching for post with id ${postDTO.getId()}...")
        Post post = postService.getOne(postDTO.getId())
        Long actualPostPoints = post.getPoints()
        Long updatedPoints = actualPostPoints + points
        log.info("Actual post points: ${actualPostPoints} - points for add ${points}")
        post.setPoints(updatedPoints)
        Post savedPost = postService.save(post)
        log.info("Post points after saving operation: ${savedPost.getPoints()}")
        addPointsToUser(points, Mapper.userToDTO(savedPost.getUser()))
    }

    @Override
    void subtractPointsFromUser(Long points, UserDTO userDTO) {
        log.info("Searching for user with id ${userDTO.getId()} for points update operation...")
        User user = userService.getOne(userDTO.getId())
        Long actualUserPoints = user.getPoints()
        Long updatedPoints = actualUserPoints - points
        log.info("Actual user points: ${actualUserPoints} - points for substract: ${points}")
        user.setPoints(updatedPoints)
        User updatedUser = userService.save(user)
        log.info("User points after saving operation: ${updatedUser.getPoints()}")
    }

    @Override
    void subtractPointsFromPost(Long points, PostDTO postDTO) {
        log.info("Searching for post with id ${postDTO.getId()} for subtract operation...")
        Post post = postService.getOne(postDTO.getId())
        Long actualPostPoints = post.getPoints()
        Long updatedPoints = actualPostPoints - points
        log.info("Actual post points: ${actualPostPoints} - points for subtract operaton: ${points}")
        post.setPoints(updatedPoints)
        Post updatedPost = postService.save(post)
        log.info("Post points after saving operation: ${updatedPost.points}")
        subtractPointsFromUser(1L, Mapper.userToDTO(post.getUser()))
    }

    @Override
    void markAnswerAsCorrect(CommentsDTO commentsDTO) {
        log.info("Searching for answer with id ${commentsDTO.getId()} - preparing \"resolve\" operation...")
        Comments comment = commentsService.getOne(commentsDTO.getId())
        log.info("Searching for post with id ${comment.getPost().getId()}...")
        Post post = postService.getOne(comment.getPost().getId())
        comment.setIsCorrect(true)
        Comments updatedComment = commentsService.save(comment)
        log.info("Answer status after update is: CORRECT - ${updatedComment.getIsCorrect()}")
        post.setResolved(true)
        Post updatedPost = postService.save(post)
        log.info("Post status after update is: RESOLVED - ${updatedPost.getResolved()}")
        addPointsToUser(2L, Mapper.userToDTO(comment.getUser()))
    }
}
