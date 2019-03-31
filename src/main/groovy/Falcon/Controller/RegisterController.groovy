package Falcon.Controller

import Falcon.Model.UserDTO
import Falcon.Service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import javax.validation.Valid

@Controller
class RegisterController {

    @Autowired
    UserService userService

    @ModelAttribute('user') //th:object
    UserDTO user(){
        new UserDTO()
    }

    @GetMapping("/registration")
    def getRegisterPage(){
        return "user/registerPage"
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    def processRegister(@ModelAttribute('user') @Valid UserDTO userRegistrationObject, BindingResult bindingResult) {

        if(bindingResult.hasErrors())
            return "registerPage"

        userService.createUser(userRegistrationObject)

        return "redirect:/index"
    }

}
