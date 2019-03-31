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
        model.addAttribute("userId", userId )
        model.addAttribute("username", authentication.getName())
        return "postsForm"
    }



    @PostMapping(value = "/creator")
    def submitPost(@Valid @ModelAttribute("post")PostDTO post, BindingResult bindingResult, Authentication authentication) {


        if(bindingResult.hasErrors())
            return "postsForm"

        post.setAuthorName(authentication.getName())


        def splittedTags = post.getTags().split(',')

        PostDTO postDTO = postService.createPost(post)

        tagsService.generateTags(splittedTags, postDTO.getId())

        Long points = 1
        userService.updateUserPoints(authentication.getName(), points)


        return "redirect:/posts"
    }


    @GetMapping("/posts")
    def getPosts(Model model, Authentication authentication) {

        Long userId = userService.getUserByName(authentication.getName()).getId()

        model.addAttribute("posts", postService.getAll().toList().reverse())
        model.addAttribute("username", authentication.getName())
        model.addAttribute("userId", userId )
        model.addAttribute("tags", tagsService.getAll().toList())

        return "posts"
    }

    @GetMapping("/edit/{id}")
    def editPost(@PathVariable Long id, Model model, Authentication authentication) {

        Long userId = userService.getUserByName(authentication.getName()).getId()
        model.addAttribute("userId", userId )
        model.addAttribute("post", postService.getPostDtoById(id))
        model.addAttribute("username", authentication.getName())


        return "editPost"
    }

    @PostMapping("/editPost/{id}")
    def createEditedPost(@Valid @ModelAttribute("post")PostDTO post, @PathVariable Long id, BindingResult bindingResult, Authentication authentication) {

        if(bindingResult.hasErrors())
            return "editPost"

        postService.updatePost(id, post)

        def newTags = post.getTags().split(',')
        List<TagsDTO> oldTags = tagsService.getTagsByPostId(id)
        tagsService.updateTags(oldTags, newTags, id)


        return "redirect:/posts"
    }

    @GetMapping("/delete/{id}")
    def deletePost(@PathVariable Long id) {

        List<TagsDTO> tags = tagsService.getTagsByPostId(id)

        tagsService.deleteTags(tags)
        postService.delete(id)
        return "redirect:/posts"
    }
}
