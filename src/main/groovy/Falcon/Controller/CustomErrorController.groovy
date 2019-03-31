package Falcon.Controller

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

import javax.servlet.http.HttpServletRequest

@Controller
class CustomErrorController implements ErrorController {
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

        model.addAttribute("auth", authentication)

        return "error/errorPage"
    }

    @Override
    String getErrorPath() {
        return "/error"
    }
}
