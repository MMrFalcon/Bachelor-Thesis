package com.falcon.forum.controller

import com.falcon.forum.model.UserDTO
import com.falcon.forum.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class UserPanelController {

    @Autowired
    UserService userService

    @RequestMapping(value = "user/panel/{userId}", method = RequestMethod.GET)
    def returnUserPanel(Authentication authentication, Model model, @PathVariable Long userId) {

        try {
            model.addAttribute("username", authentication.getName())
        }catch (NullPointerException exception) {  // FIXME  add slf4j log
            return "home/index"
        }


        UserDTO user = userService.getUserByName(authentication.getName())

        model.addAttribute("points", user.getPoints())
        return "user/userPanel"
    }

}
