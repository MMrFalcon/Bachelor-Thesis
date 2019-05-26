package com.falcon.forum.service.implementation

import com.falcon.forum.exception.VoteAlreadyAddedException
import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.Comments
import com.falcon.forum.persist.Post
import com.falcon.forum.persist.User
import com.falcon.forum.service.*
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

@Slf4j
@Service
@Transactional
class PointsServiceImplementation implements PointsService {

    private final UserService userService
    private final CommentsService commentsService
    private final PostService postService
    private final VoteService voteService

    PointsServiceImplementation(UserService userService, CommentsService commentsService, PostService postService,
                                VoteService voteService) {
        this.userService = userService
        this.commentsService = commentsService
        this.postService = postService
        this.voteService = voteService
    }

    @Override
    void addPointsToUser(Long points, UserDTO userDTO) {
        log.info("Transaction status: ${TransactionSynchronizationManager.actualTransactionActive} - " +
                "${TransactionSynchronizationManager.currentTransactionName}")
        log.info("Searching for user with id ${userDTO.getId()} for points update operation...")
        User user = userService.getOne(userDTO.getId())
        Long actualUserPoints = user.getPoints()
        Long updatedPoints = actualUserPoints + points
        log.info("Actual user points: ${actualUserPoints} - points for add: ${points}")
        user.setPoints(updatedPoints)
        User updatedUser = userService.save(user)
        log.info("User points after saving operation: ${updatedUser.getPoints()}")
        addPlusPointsToUser(points, Mapper.userToDTO(updatedUser))
    }

    @Override
    void addPlusPointsToUser(Long points, UserDTO userDTO) {
        log.info("Transaction status: ${TransactionSynchronizationManager.actualTransactionActive} - " +
                "${TransactionSynchronizationManager.currentTransactionName}")
        log.info("Searching for user with id ${userDTO.getId()} for points update operation...")
        User user = userService.getOne(userDTO.getId())
        Long actualUserPoints = user.getPlusPoints()
        Long updatedPoints = actualUserPoints + points
        log.info("Actual user plus points: ${actualUserPoints} - points for add: ${points}")
        user.setPlusPoints(updatedPoints)
        User updatedUser = userService.save(user)
        log.info("User plus points after saving operation: ${updatedUser.getPlusPoints()}")
    }

    @Override
    void addMinusPointsToUser(Long points, UserDTO userDTO) {
        log.info("Transaction status: ${TransactionSynchronizationManager.actualTransactionActive} - " +
                "${TransactionSynchronizationManager.currentTransactionName}")
        log.info("Searching for user with id ${userDTO.getId()} for points update operation...")
        User user = userService.getOne(userDTO.getId())
        Long actualUserPoints = user.getMinusPoints()
        Long updatedPoints = actualUserPoints + points
        log.info("Actual user minus points: ${actualUserPoints} - points for add: ${points}")
        user.setMinusPoints(updatedPoints)
        User updatedUser = userService.save(user)
        log.info("User minus points after saving operation: ${updatedUser.getMinusPoints()}")
    }

    @Override
    void addPointsToPost(Long points, PostDTO postDTO, String voteAuthorName) {
        log.info("Transaction status: ${TransactionSynchronizationManager.actualTransactionActive} - " +
                "${TransactionSynchronizationManager.currentTransactionName}")

        log.info("Searching for post with id ${postDTO.getId()}...")
        Post post = postService.getOne(postDTO.getId())
        Long actualPostPoints = post.getPoints()
        Long updatedPoints = actualPostPoints + points
        log.info("Actual post points: ${actualPostPoints} - points for add ${points}")
        post.setPoints(updatedPoints)
        Post savedPost = postService.save(post)
        log.info("Post points after saving operation: ${savedPost.getPoints()}")
        addPointsToUser(points, Mapper.userToDTO(savedPost.getUser()))
        UserDTO voteAuthor = userService.getUserByName(voteAuthorName)
        voteService.addVoteToPost(voteAuthor, Mapper.postToDto(savedPost))
    }

    @Override
    void subtractPointsFromUser(Long points, UserDTO userDTO) {
        log.info("Transaction status: ${TransactionSynchronizationManager.actualTransactionActive} - " +
                "${TransactionSynchronizationManager.currentTransactionName}")
        log.info("Searching for user with id ${userDTO.getId()} for points update operation...")
        User user = userService.getOne(userDTO.getId())
        Long actualUserPoints = user.getPoints()
        Long updatedPoints = actualUserPoints - points
        log.info("Actual user points: ${actualUserPoints} - points for substract: ${points}")
        user.setPoints(updatedPoints)
        User updatedUser = userService.save(user)
        log.info("User points after saving operation: ${updatedUser.getPoints()}")
        addMinusPointsToUser(points, Mapper.userToDTO(updatedUser))
    }

    @Override
    void subtractPointsFromPost(Long points, PostDTO postDTO, String voteAuthorName) {
        log.info("Transaction status: ${TransactionSynchronizationManager.actualTransactionActive} - " +
                "${TransactionSynchronizationManager.currentTransactionName}")
        log.info("Searching for post with id ${postDTO.getId()} for subtract operation...")
        Post post = postService.getOne(postDTO.getId())
        Long actualPostPoints = post.getPoints()
        Long updatedPoints = actualPostPoints - points
        log.info("Actual post points: ${actualPostPoints} - points for subtract operaton: ${points}")
        post.setPoints(updatedPoints)
        Post updatedPost = postService.save(post)
        log.info("Post points after saving operation: ${updatedPost.points}")
        subtractPointsFromUser(1L, Mapper.userToDTO(post.getUser()))
        UserDTO voteAuthor = userService.getUserByName(voteAuthorName)
        voteService.addVoteToPost(voteAuthor, Mapper.postToDto(updatedPost))
    }

    @Override
    void addPointsToAnswer(Long points, CommentsDTO commentsDTO, String voteAuthorName) {
        log.info("Transaction status: ${TransactionSynchronizationManager.actualTransactionActive} - " +
                "${TransactionSynchronizationManager.currentTransactionName}")
        log.info("Searching for answer with id ${commentsDTO.getId()}...")
        Comments comments = commentsService.getOne(commentsDTO.getId())
        Long actualAnswerPoints = comments.getPoints()
        Long updatedPoints = actualAnswerPoints + points
        log.info("Actual answer points: ${actualAnswerPoints} - points for add ${points}")
        comments.setPoints(updatedPoints)
        Comments updatedAnswer = commentsService.save(comments)
        log.info("Answer points after saving operation: ${updatedAnswer.getPoints()}")
        addPointsToUser(points, Mapper.userToDTO(updatedAnswer.getUser()))
        UserDTO voteAuthor = userService.getUserByName(voteAuthorName)
        voteService.addVoteToComment(voteAuthor, Mapper.commentsToDto(updatedAnswer))
    }

    @Override
    void subtractPointsFromAnswer(Long points, CommentsDTO commentsDTO, String voteAuthorName) {
        log.info("Transaction status: ${TransactionSynchronizationManager.actualTransactionActive} - " +
                "${TransactionSynchronizationManager.currentTransactionName}")
        log.info("Searching for answer with id ${commentsDTO.getId()} for subtract operation...")
        Comments comments = commentsService.getOne(commentsDTO.getId())
        Long actualAnswerPoints = comments.getPoints()
        Long updatedPoints = actualAnswerPoints - points
        log.info("Actual answer points: ${actualAnswerPoints} - points for subtract operaton: ${points}")
        comments.setPoints(updatedPoints)
        Comments updatedAnswer = commentsService.save(comments)
        log.info("Answer points after saving operation: ${updatedAnswer.points}")
        subtractPointsFromUser(1L, Mapper.userToDTO(updatedAnswer.getUser()))
        UserDTO voteAuthor = userService.getUserByName(voteAuthorName)
        voteService.addVoteToComment(voteAuthor, Mapper.commentsToDto(updatedAnswer))
    }

    @Override
    void markAnswerAsCorrect(CommentsDTO commentsDTO) {
        log.info("Transaction status: ${TransactionSynchronizationManager.actualTransactionActive} - " +
                "${TransactionSynchronizationManager.currentTransactionName}")
        log.info("Searching for answer with id ${commentsDTO.getId()} - preparing \"resolve\" operation...")
        Comments comment = commentsService.getOne(commentsDTO.getId())
        if (comment.getIsCorrect()) {
            throw new VoteAlreadyAddedException("Comment was already marked as correct")
        }
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
