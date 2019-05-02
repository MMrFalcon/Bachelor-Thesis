package Falcon.Controller

import Falcon.Exceptions.DuplicateEmailException
import Falcon.Exceptions.DuplicateUsernameException
import Falcon.Model.UserDTO
import Falcon.Service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import javax.validation.Valid

@Controller
class RegisterController {

    @Autowired
    UserService userService

    @GetMapping("/registration")
    def getRegisterPage(UserDTO userDTO) {
        return "user/registerPage"
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    def processRegister(@Valid UserDTO userDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "user/registerPage"
        }

        try {
            userService.createUser(userDTO)
        } catch(DuplicateUsernameException exception) {
            model.addAttribute("userExist", true)
            model.addAttribute("userDTO", new UserDTO())
            return "user/registerPage"
        } catch (DuplicateEmailException exception) {
            model.addAttribute("emailExist", true)
            model.addAttribute("userDTO", new UserDTO())
            return "user/registerPage"
        }


        return "redirect:/index"
    }

}
