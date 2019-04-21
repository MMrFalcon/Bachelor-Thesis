package Falcon.Controller

import Falcon.Service.UserService
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletRequest

@Controller
class CustomErrorController implements ErrorController {
    private UserService userService

    CustomErrorController(UserService userService) {
        this.userService = userService
    }

    @RequestMapping("/error")
    def getError(HttpServletRequest servletRequest, Model model, Authentication authentication) {
        def status = servletRequest.getAttribute("javax.servlet.error.status_code")
        def exception = (Exception) servletRequest.getAttribute("javax.servlet.error.exception")

        model.addAttribute("status", status)

        if(!exception){
            model.addAttribute("exception", "There is no 'EXCEPTION' here")
        }else {
            model.addAttribute("exception", exception.getMessage())
        }

        if (authentication) {
            model.addAttribute("auth", authentication)
            model.addAttribute("userId", userService.getUserByName(authentication.getName()).getId())
        }

        return "error/errorPage"
    }

    @Override
    String getErrorPath() {
        return "/error"
    }
}
