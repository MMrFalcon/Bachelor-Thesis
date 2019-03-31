package Falcon.Controller

import Falcon.Model.UserDTO
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
class LoginController {

    @ModelAttribute('user')
    UserDTO user(){
        new UserDTO()
    }

    @RequestMapping(value=["/","/index","/login","/home"], method = RequestMethod.GET)
    def index() {
        return "home/index"
    }




}
