package Falcon.Controller

import Falcon.Model.UserDTO
import Falcon.Service.UserService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class LoginController {

    private final UserService userService;

    @ModelAttribute('user')
    UserDTO user(){
        new UserDTO()
    }

    @RequestMapping(value=["/","/index","/login","/home"], method = RequestMethod.GET)
    def index(Authentication authentication) {

        if (authentication != null) {
            UserDTO userDTO = userService.getUserByName(authentication.getName())
            return "user/panel/${userDTO.getId()}"
        }

        return "home/index"
    }




}
