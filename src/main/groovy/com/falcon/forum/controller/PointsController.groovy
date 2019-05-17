package com.falcon.forum.controller

import com.falcon.forum.service.CommentsService
import com.falcon.forum.service.PointsService
import com.falcon.forum.service.PostService
import com.falcon.forum.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class PointsController {
    private final PointsService pointsService
    private final UserService userService
    private final PostService postService
    private final CommentsService commentsService

    PointsController(PointsService pointsService, UserService userService, PostService postService, CommentsService commentsService) {
        this.pointsService = pointsService
        this.userService = userService
        this.postService = postService
        this.commentsService = commentsService
    }

    @GetMapping("/voteup/{answerId}")
    def addPointsToAnswer(@PathVariable Long answerId){

    }

    @GetMapping("/votedown/{answerId}")
    def subtractPointsFromAnswer(@PathVariable Long answerId) {

    }

    @GetMapping("/correctAnswer/{answerId}")
    def markAnswerAsCorrect(@PathVariable Long answerId) {

    }

    @GetMapping("/voteupPost/{postId}")
    def addPointsToPost(@PathVariable Long postId) {

    }

    @GetMapping("/votedownPost/postId")
    def subtractPointsFromPost(@PathVariable Long postId) {

    }

}
