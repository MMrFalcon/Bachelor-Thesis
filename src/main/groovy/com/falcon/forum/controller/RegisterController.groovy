package com.falcon.forum.controller

import com.falcon.forum.exception.DuplicateEmailException
import com.falcon.forum.exception.DuplicateUsernameException
import com.falcon.forum.exception.PasswordsException
import com.falcon.forum.model.UserDTO
import com.falcon.forum.service.UserService
import groovy.util.logging.Slf4j
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import javax.validation.Valid

@Slf4j
@Controller
class RegisterController {

    private final UserService userService

    RegisterController(UserService userService) {
        this.userService = userService
    }

    @GetMapping("/registration")
    def getRegisterPage(UserDTO userDTO) {
        return "user/registerPage"
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    def processRegister(@Valid @ModelAttribute('userDTO')UserDTO userDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "user/registerPage"
        }

        try {
            userService.createUser(userDTO)
            return "redirect:/registerSuccess"
        } catch(DuplicateUsernameException exception) {
            log.warn(exception.getMessage())
            model.addAttribute("userExist", true)
            model.addAttribute("userDTO", new UserDTO())
            return "user/registerPage"
        } catch (DuplicateEmailException exception) {
            log.warn(exception.getMessage())
            model.addAttribute("emailExist", true)
            model.addAttribute("userDTO", new UserDTO())
            return "user/registerPage"
        } catch (PasswordsException ex) {
            log.warn(ex.getMessage())
            model.addAttribute("passwordException", true)
            model.addAttribute("userDTO", new UserDTO())
            return "user/registerPage"
        }

    }

    @GetMapping("/registerSuccess")
    def registerSuccess() {
        return "user/registerSuccess"
    }

}
