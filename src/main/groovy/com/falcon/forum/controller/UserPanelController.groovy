package com.falcon.forum.controller

import com.falcon.forum.model.UserDTO
import com.falcon.forum.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Slf4j
@Controller
class UserPanelController {

    private final UserService userService

    UserPanelController(UserService userService) {
        this.userService = userService
    }

    @GetMapping("/user/panel/{userId}")
    def returnUserPanel(Authentication authentication, Model model, @PathVariable Long userId) {

            model.addAttribute("username", authentication.getName())
            UserDTO user = userService.getUserByName(authentication.getName())

            model.addAttribute("points", user.getPoints())
            model.addAttribute("correctAnswers", userService.getCountOfCorrectAnswers(user))
            model.addAttribute("postsPoints", userService.getPostsPoints(user))
            model.addAttribute("answersPoints", userService.getAnswersPoints(user))
            model.addAttribute("negativePoints", user.minusPoints)
            model.addAttribute("addedPosts", userService.getNumberOfAddedPosts(user))
            model.addAttribute("addedAnswers", userService.getNumberOfAddedAnswers(user))
            model.addAttribute("posts", userService.getPostsDto(user))
            model.addAttribute("answers", userService.getCommentsDto(user))
            return "user/userPanel"
    }

}
