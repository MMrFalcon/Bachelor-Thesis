package com.falcon.forum.controller

import com.falcon.forum.model.PostDTO
import com.falcon.forum.service.*
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
class PostController {

    private final PostService postService
    private final UserService userService
    private final TagsService tagsService
    private final CommentsService commentsService
    private final PointsService pointsService
    private final VoteService voteService

    PostController(PostService postService, UserService userService, TagsService tagsService,
                   CommentsService commentsService, PointsService pointsService, VoteService voteService) {
        this.postService = postService
        this.userService = userService
        this.tagsService = tagsService
        this.commentsService = commentsService
        this.pointsService = pointsService
        this.voteService = voteService
    }


    @GetMapping("/creator")
    def getForm(Model model, Authentication authentication) {
        Long userId = userService.getUserByName(authentication.getName()).getId()
        model.addAttribute("post", new PostDTO())
        model.addAttribute("userId", userId )
        model.addAttribute("username", authentication.getName())
        return "post/postsForm"
    }



    @PostMapping(value = "/creator")
    def submitPost(@Valid @ModelAttribute("post")PostDTO post, BindingResult bindingResult, Authentication authentication) {

        if(bindingResult.hasErrors())
            return "post/postsForm"

       PostDTO postDTO =  postService.createPost(post, userService.getUserByName(authentication.getName()))

        return "redirect:/tags/creator/" + postDTO.getId()
    }

    @GetMapping("/posts")
    def getPosts(Model model, Authentication authentication) {

        Long userId = userService.getUserByName(authentication.getName()).getId()

        model.addAttribute("posts", postService.getAll().toList().reverse())
        model.addAttribute("username", authentication.getName())
        model.addAttribute("userId", userId )
        model.addAttribute("tags", tagsService.getAll().toList())

        return "post/posts"
    }

    @GetMapping("/posts/post/{id}")
    def showPost(@PathVariable Long id, Model model, Authentication authentication) {
        final userName = authentication.getName()
        Long userId = userService.getUserByName(userName).getId()
        PostDTO post = postService.getPostDtoById(id)
        boolean deletable = postService.isDeletable(post, userName)
        boolean editable = postService.isEditable(post, userName)
        boolean isAuthor = postService.isAuthor(post, userName)
        model.addAttribute("postId", id)
        model.addAttribute("userId", userId )
        model.addAttribute("post", post)
        model.addAttribute("username", userName)
        model.addAttribute("author", postService.getAuthorName(post))
        model.addAttribute("delete", deletable)
        model.addAttribute("edit", editable)
        model.addAttribute("answers", commentsService.getComments(post))
        model.addAttribute("isAuthor", isAuthor)
        model.addAttribute("isPostVoteAuthor", voteService.isPostAuthor(userName, post.getId()))
        model.addAttribute("voteService", voteService)
        return "post/post"
    }

    @GetMapping("/edit/{id}")
    def editPost(@PathVariable Long id, Model model, Authentication authentication) {

        Long userId = userService.getUserByName(authentication.getName()).getId()
        model.addAttribute("userId", userId )
        model.addAttribute("post", postService.getPostDtoById(id))
        model.addAttribute("username", authentication.getName())

        return "post/editPost"
    }

    @PostMapping("/editPost/{id}")
    def createEditedPost(@Valid @ModelAttribute("post")PostDTO post, @PathVariable Long id, BindingResult bindingResult,
                         Authentication authentication) {

        if(bindingResult.hasErrors())
            return "post/editPost"

        postService.updatePost(id, post)
        return "redirect:/tags/creator/edit/" + id
    }

    @GetMapping("/delete/{id}")
    def deletePost(@PathVariable Long id) {
        postService.delete(id)
        return "redirect:/posts"
    }

}
