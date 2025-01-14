package ma.emsi.gestionnairedestaches.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ma.emsi.gestionnairedestaches.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @GetMapping(path="/")
    public String index(HttpServletRequest request){
            return "redirect:/project";
    }

    @GetMapping(path="/error500")
    public String error500(Model model){ return "Error/pages-error-500"; }

    @GetMapping(path="/error404")
    public String error404(Model model){ return "Error/pages-error"; }

    @GetMapping(path="/index")
    public String index(Model model, HttpSession session){
        User user = (User) session.getAttribute("connectedUser");
        model.addAttribute("user", user);
        return "Main/index";
    }
}
