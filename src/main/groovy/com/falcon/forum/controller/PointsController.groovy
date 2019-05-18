package com.falcon.forum.controller

import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.service.CommentsService
import com.falcon.forum.service.PointsService
import com.falcon.forum.service.PostService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class PointsController {
    private final PointsService pointsService
    private final PostService postService
    private final CommentsService commentsService
    final Long VOTE_UP_POINTS = 1L
    final Long VOTE_DOWN_POINTS = 1L

    PointsController(PointsService pointsService, PostService postService, CommentsService commentsService) {
        this.pointsService = pointsService
        this.postService = postService
        this.commentsService = commentsService
    }

    @GetMapping("/voteup/{answerId}")
    def addPointsToAnswer(@PathVariable Long answerId){
        CommentsDTO answer = commentsService.getCommentDtoById(answerId)
        pointsService.addPointsToAnswer(VOTE_UP_POINTS, answer)
        Long postId = postService.getPostByAnswer(answer).getId()
        return "redirect:/posts/post/${postId}"
    }

    @GetMapping("/votedown/{answerId}")
    def subtractPointsFromAnswer(@PathVariable Long answerId) {
        CommentsDTO answer = commentsService.getCommentDtoById(answerId)
        pointsService.subtractPointsFromAnswer(VOTE_DOWN_POINTS, answer)
        Long postId = postService.getPostByAnswer(answer).getId()
        return "redirect:/posts/post/${postId}"
    }

    @GetMapping("/correctAnswer/{answerId}")
    def markAnswerAsCorrect(@PathVariable Long answerId) {
        CommentsDTO answer = commentsService.getCommentDtoById(answerId)
        pointsService.markAnswerAsCorrect(answer)
        Long postId = postService.getPostByAnswer(answer).getId()
        return "redirect:/posts/post/${postId}"
    }

    @GetMapping("/voteupPost/{postId}")
    def addPointsToPost(@PathVariable Long postId) {
        PostDTO post = postService.getPostDtoById(postId)
        pointsService.addPointsToPost(VOTE_UP_POINTS, post)
        return "redirect:/posts/post/${postId}"
    }

    @GetMapping("/votedownPost/{postId}")
    def subtractPointsFromPost(@PathVariable Long postId) {
        PostDTO post = postService.getPostDtoById(postId)
        pointsService.subtractPointsFromPost(VOTE_DOWN_POINTS, post)
        return "redirect:/posts/post/${postId}"
    }

}
