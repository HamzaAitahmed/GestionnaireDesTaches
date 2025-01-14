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
    public static final String CONNECTED_USER = "connectedUser";
    public static final String ACTIVE = "active";
    public static final String CHANGE_PWD = "changePWD";
    public static final String PATH_USER_PROFILE_EDIT = "Main/UserPages/user-profile-edit";
    public static final String REDIRECT_TASK = "redirect:/task";

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path="/userList")
    public String userList(Model model, HttpSession session){
        User user = (User) session.getAttribute(CONNECTED_USER);
        model.addAttribute("user", user);
        return "Main/UserPages/user-list";
    }

    @GetMapping(path="/userProfil")
    public String userProfil(Model model, HttpSession session){
        User user = (User) session.getAttribute(CONNECTED_USER);
        model.addAttribute("user", user);
        return "Main/UserPages/user-profile";
    }

    @GetMapping(path="/userProfileEdit")
    public String userProfileEdit(Model model, HttpSession session){
        User user = (User) session.getAttribute(CONNECTED_USER);
        model.addAttribute("user", user);
        return PATH_USER_PROFILE_EDIT;
    }

    @PostMapping(path="/changePWD")
    public String changePWD(Model model, HttpSession session,
                        @RequestParam(name = "currentPWD" ) String currentPWD,
                        @RequestParam(name = "newPWD" ) String newPWD,
                        @RequestParam(name = "ConfirmationPWD" ) String confirmationPWD)
    {
        User user = (User) session.getAttribute(CONNECTED_USER);
        if ((user.getPassword() == null || user.getPassword().equals(currentPWD)) && confirmationPWD.equals(newPWD))
        {
            user.setPassword(newPWD);
            userRepository.save(user);
            model.addAttribute("user", user);
            model.addAttribute(ACTIVE, CHANGE_PWD);
            return PATH_USER_PROFILE_EDIT;
        }

        model.addAttribute("user", user);
        model.addAttribute(ACTIVE, CHANGE_PWD);
        return "redirect:/userProfileEdit";

    }

    @PostMapping(path="/PersonnalInfo")
    public String personnalInfo(Model model, HttpSession session )
    {
        User user = (User) session.getAttribute(CONNECTED_USER);
        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute(ACTIVE, CHANGE_PWD);
        return "redirect:/userProfileEdit";
    }
}
