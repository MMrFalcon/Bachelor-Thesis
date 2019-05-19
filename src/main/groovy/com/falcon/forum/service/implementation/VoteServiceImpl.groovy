package com.falcon.forum.service.implementation

import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.persist.Comments
import com.falcon.forum.persist.Post
import com.falcon.forum.persist.User
import com.falcon.forum.service.CommentsService
import com.falcon.forum.service.PostService
import com.falcon.forum.service.UserService
import com.falcon.forum.service.VoteService
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service

@Service
@Slf4j
class VoteServiceImpl implements VoteService {

    private final UserService userService
    private final PostService postService
    private final CommentsService commentsService

    VoteServiceImpl(UserService userService, PostService postService, CommentsService commentsService) {
        this.userService = userService
        this.postService = postService
        this.commentsService = commentsService
    }

    @Override
    void addVoteToPost(UserDTO voteAuthor, PostDTO post) {
        User user = userService.getOne(voteAuthor.getId())
        Post postEntity = postService.getOne(post.getId())
        log.info("Adding user with id ${user.getId()} to the vote Set.")
        postEntity.getPostUsersVotes().add(user)
        Post savedPost = postService.saveAndFlush(postEntity)
        log.info("Post votes size after flushing operation: ${savedPost.getPostUsersVotes().size()}")
    }

    @Override
    void addVoteToComment(UserDTO voteAuthor, CommentsDTO comment) {
        User user = userService.getOne(voteAuthor.getId())
        Comments commentsEntity = commentsService.getOne(comment.getId())
        log.info("Adding user with id ${user.getId()} to the vote Set.")
        commentsEntity.getAnswerUsersVotes().add(user)
        Comments savedComment = commentsService.saveAndFlush(commentsEntity)
        log.info("Comment votes size after flushing operation: ${savedComment.getAnswerUsersVotes().size()}")
    }
}
