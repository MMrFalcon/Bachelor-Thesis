package com.falcon.forum.controller

import com.falcon.forum.model.UserDTO
import com.falcon.forum.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class LoginController {

    private final UserService userService

    LoginController(UserService userService) {
        this.userService = userService
    }

    @ModelAttribute('user')
    UserDTO user(){
        new UserDTO()
    }

    @RequestMapping(value=["/","/index","/login","/home"], method = RequestMethod.GET)
    def index(Authentication authentication) {

        if (authentication != null) {
            UserDTO userDTO = userService.getUserByName(authentication.getName())
            return "redirect:/user/panel/${userDTO.getId()}"
        }

        return "home/index"
    }




}
