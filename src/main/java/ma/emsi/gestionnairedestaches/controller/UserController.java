package ma.emsi.gestionnairedestaches.controller;

import jakarta.servlet.http.HttpSession;
import ma.emsi.gestionnairedestaches.model.*;
import ma.emsi.gestionnairedestaches.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path="/userList")
    public String userList(Model model, HttpSession session){
        User user = (User) session.getAttribute("connectedUser");
        model.addAttribute("user", user);
        return "Main/UserPages/user-list";
    }

    @GetMapping(path="/userProfil")
    public String userProfil(Model model, HttpSession session){
        User user = (User) session.getAttribute("connectedUser");
        model.addAttribute("user", user);
        return "Main/UserPages/user-profile";
    }

    @GetMapping(path="/userProfileEdit")
    public String userProfileEdit(Model model, HttpSession session){
        User user = (User) session.getAttribute("connectedUser");
        model.addAttribute("user", user);
        return "Main/UserPages/user-profile-edit";
    }

    @PostMapping(path="/changePWD")
    public String changePWD(Model model, HttpSession session,
                        @RequestParam(name = "currentPWD" ) String currentPWD,
                        @RequestParam(name = "newPWD" ) String newPWD,
                        @RequestParam(name = "ConfirmationPWD" ) String ConfirmationPWD)
    {
        User user = (User) session.getAttribute("connectedUser");
        if(user.getPassword()!=null && user.getPassword().equals(currentPWD))
        {
            if (ConfirmationPWD.equals(newPWD)) {
                user.setPassword(newPWD);
                userRepository.save(user);
                model.addAttribute("user", user);
                model.addAttribute("active", "changePWD");
                return "Main/UserPages/user-profile-edit";
            }
        }else {
            if (ConfirmationPWD.equals(newPWD)) {
                user.setPassword(newPWD);
                userRepository.save(user);
                model.addAttribute("user", user);
                model.addAttribute("active", "changePWD");
                return "Main/UserPages/user-profile-edit";
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("active", "changePWD");
        return "redirect:/userProfileEdit";

    }

    @PostMapping(path="/PersonnalInfo")
    public String personnalInfo(Model model, HttpSession session )
    {
        User user = (User) session.getAttribute("connectedUser");
        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("active", "changePWD");
        return "redirect:/userProfileEdit";
    }
}
