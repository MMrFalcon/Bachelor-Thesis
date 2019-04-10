package Falcon.Controller

import Falcon.Model.PostDTO
import Falcon.Model.TagsDTO
import Falcon.Service.PostService
import Falcon.Service.TagsService
import Falcon.Service.UserService
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

    private PostService postService
    private UserService userService
    private TagsService tagsService

    PostController(PostService postService, UserService userService, TagsService tagsService) {
        this.postService = postService
        this.userService = userService
        this.tagsService = tagsService
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

        Set<TagsDTO> tagsDTOs = tagsService.generateTags(tagsDTO.getTag(), postService.getPostDtoById(postId))
        PostDTO postDTO = postService.addPostTags(postId,tagsDTOs)

        if (postDTO)
            userService.updateUserPoints(authentication.getName(), 1L)

        return "redirect:/posts"
    }


    @GetMapping("/posts")
    def getPosts(Model model, Authentication authentication) {

        Long userId = userService.getUserByName(authentication.getName()).getId()

        model.addAttribute("posts", postService.getAll().toList().reverse())
        model.addAttribute("username", authentication.getName())
        model.addAttribute("userId", userId ) // for delete
        model.addAttribute("tags", tagsService.getAll().toList())

        return "post/posts"
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

    @GetMapping("/tags/creator/edit/{postId}")
    def editTags(@PathVariable Long postId, Model model, Authentication authentication) {
        Long userId = userService.getUserByName(authentication.getName()).getId()

        def tagsString = tagsService.getPostTagsAsString(postId)
        model.addAttribute("tags", new TagsDTO())
        model.addAttribute("tag", tagsString)
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

        Set<TagsDTO> tagsDTOs = tagsService.generateTags(tagsDTO.getTag(), postService.getPostDtoById(postId))
        postService.updatePostTags(postId,tagsDTOs)

        return "redirect:/posts"
    }

    @GetMapping("/delete/{id}")
    def deletePost(@PathVariable Long id) {
        postService.delete(id)
        return "redirect:/posts"
    }
}
