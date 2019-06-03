package com.falcon.forum.controller

import com.falcon.forum.model.PostDTO
import com.falcon.forum.model.TagsDTO
import com.falcon.forum.service.PointsService
import com.falcon.forum.service.PostService
import com.falcon.forum.service.TagsService
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
class TagsController {

    private final UserService userService
    private final TagsService tagsService
    private final PostService postService
    private final PointsService pointsService

    TagsController(UserService userService, TagsService tagsService, PostService postService, PointsService pointsService) {
        this.userService = userService
        this.tagsService = tagsService
        this.postService = postService
        this.pointsService = pointsService
    }

    @GetMapping(value = "/tags/creator/{postId}")
    def getTagsForm(@PathVariable Long postId, Model model, Authentication authentication) {
        Long userId = userService.getUserByName(authentication.getName()).getId()
        model.addAttribute("tags", new TagsDTO())
        model.addAttribute("username", authentication.getName())
        model.addAttribute("userId", userId )
        model.addAttribute("postId", postId)
        return "post/tagsForm"
    }

    @PostMapping(value = "/tags/creator/{postId}")
    def sendPostedTags(@Valid @ModelAttribute("tags") TagsDTO tagsDTO, @PathVariable Long postId,
                       BindingResult bindingResult, Authentication authentication) {

        if(bindingResult.hasErrors())
            return "redirect:/tags/creator/" + postId

        Set<TagsDTO> tagsDTOs = tagsService.generateTags(tagsDTO.getTag())
        PostDTO postDTO = postService.addPostTags(postId,tagsDTOs)

        if (postDTO)
            pointsService.addPointsToUser(1L, userService.getUserByName(authentication.getName()))

        return "redirect:/posts/post/${postDTO.getId()}"
    }

    @GetMapping("/tags/creator/edit/{postId}")
    def editTags(@PathVariable Long postId, Model model, Authentication authentication) {
        Long userId = userService.getUserByName(authentication.getName()).getId()

        def tagsString = tagsService.getPostTagsAsString(postId)
        TagsDTO tag =  new TagsDTO(tagsString)
        model.addAttribute("tags", tag)
        model.addAttribute("username", authentication.getName())
        model.addAttribute("userId", userId )
        model.addAttribute("postId", postId)
        return "post/editTags"
    }

    @PostMapping("/tags/creator/edit/{postId}")
    def updateTags(@Valid @ModelAttribute("tags") TagsDTO tagsDTO, @PathVariable Long postId,
                   BindingResult bindingResult, Authentication authentication) {

        if(bindingResult.hasErrors())
            return "redirect:/tags/creator/edit/" + postId

        Set<TagsDTO> tagsDTOs = tagsService.generateTags(tagsDTO.getTag())
        postService.updatePostTags(postId,tagsDTOs)

        return "redirect:/posts/post/${postId}"
    }
}
