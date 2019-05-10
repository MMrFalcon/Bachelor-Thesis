package com.falcon.forum.controller

import com.falcon.forum.model.CommentsDTO
import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.UserDTO
import com.falcon.forum.service.CommentsService
import com.falcon.forum.service.PostService
import com.falcon.forum.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

import javax.validation.Valid

@Controller
class CommentsController {

    private final UserService userService
    private final PostService postService
    private final CommentsService commentsService

    CommentsController(UserService userService, PostService postService, CommentsService commentsService) {
        this.userService = userService
        this.postService = postService
        this.commentsService = commentsService
    }

    @GetMapping("/posts/post/{id}/answer")
    def getAnswerForm(@PathVariable Long id, Model model, Authentication authentication) {
        final userName = authentication.getName()
        Long userId = userService.getUserByName(userName).getId()
        PostDTO post = postService.getPostDtoById(id)
        boolean deletable = postService.isDeletable(post, userName)
        boolean editable = postService.isEditable(post, userName)
        model.addAttribute("userId", userId )
        model.addAttribute("post", post)
        model.addAttribute("username", userName)
        model.addAttribute("author", postService.getAuthorName(post))
        model.addAttribute("delete", deletable)
        model.addAttribute("edit", editable)
        model.addAttribute("comment", new CommentsDTO())
        model.addAttribute("postId", id)
        model.addAttribute("answers", commentsService.getComments(post))
        return "post/answerForm"
    }

    @PostMapping("/posts/post/{id}/answer")
    def postAnswer(@Valid @ModelAttribute("comment") CommentsDTO commentsDTO, BindingResult bindingResult,
                   Authentication authentication, @PathVariable Long id, Model model) {

        if (bindingResult.hasErrors()) {
            return "redirect:/posts/post/${id}/answer"
        }
        final userName = authentication.getName()
        Long userId = userService.getUserByName(userName).getId()
        PostDTO postDTO = postService.getPostDtoById(id)

        model.addAttribute("userId", userId )
        model.addAttribute("post", postDTO)
        model.addAttribute("username", userName)
        model.addAttribute("author", postService.getAuthorName(postDTO))
        model.addAttribute("postId", id)
        model.addAttribute("answers", commentsService.getComments(postDTO))
        UserDTO user = userService.getUserByName(authentication.getName())
        PostDTO post = postService.getPostDtoById(id)
        commentsService.createComment(commentsDTO, user, post)
        return "redirect:/posts/post/${id}"
    }

    @GetMapping("/posts/post/{id}/answer/{answerId}/edit")
    def editAnswer(@PathVariable Long id, @PathVariable Long answerId, Model model, Authentication authentication) {
        final userName = authentication.getName()
        Long userId = userService.getUserByName(userName).getId()
        PostDTO post = postService.getPostDtoById(id)
        boolean deletable = postService.isDeletable(post, userName)
        boolean editable = postService.isEditable(post, userName)
        model.addAttribute("userId", userId )
        model.addAttribute("post", post)
        model.addAttribute("username", userName)
        model.addAttribute("author", postService.getAuthorName(post))
        model.addAttribute("delete", deletable)
        model.addAttribute("edit", editable)
        model.addAttribute("comment", commentsService.getCommentDtoById(answerId))
        model.addAttribute("postId", id)
        model.addAttribute("answers", commentsService.getComments(post))
        model.addAttribute("answerId", answerId)
        return "post/editAnswerForm"
    }

    @PostMapping("/posts/post/{id}/answer/{answerId}/edit")
    def postEditedAnswer(@Valid @ModelAttribute("comment") CommentsDTO commentsDTO, @PathVariable Long id,
                         @PathVariable Long answerId, Model model, BindingResult bindingResult,
                         Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "redirect:/posts/post/${id}/answer/${answerId}/edit"
        }
        final userName = authentication.getName()
        Long userId = userService.getUserByName(userName).getId()
        PostDTO postDTO = postService.getPostDtoById(id)

        model.addAttribute("userId", userId )
        model.addAttribute("post", postDTO)
        model.addAttribute("username", userName)
        model.addAttribute("author", postService.getAuthorName(postDTO))
        model.addAttribute("postId", id)
        model.addAttribute("answers", commentsService.getComments(postDTO))
        model.addAttribute("answerId", answerId)
        commentsService.updateComment(commentsDTO, answerId)
        return "redirect:/posts/post/${id}"
    }

}
