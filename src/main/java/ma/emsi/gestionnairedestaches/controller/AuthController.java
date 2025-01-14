package ma.emsi.gestionnairedestaches.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ma.emsi.gestionnairedestaches.model.User;
import ma.emsi.gestionnairedestaches.repository.UserRepository;
import ma.emsi.gestionnairedestaches.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public static final String REDIRECT_REGISTER_ERROR = "redirect:/register?error";
    public static final String REDIRECT_LOGIN = "redirect:/login";

    public AuthController(UserRepository userRepository ,UserService userService,AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @GetMapping(path="/home")
    public String home(){ return "Auth/home"; }

    @GetMapping(path="/login")
    public String login(){
        return "Auth/login";
    }

    @PostMapping(path="/login")
    public String login(HttpServletRequest request, @ModelAttribute("email" ) String email, @ModelAttribute("password") String password){
        try {
            User authenticatedUser = userRepository.findUserByEmail(email);
            if(authenticatedUser == null){
                     return REDIRECT_LOGIN;
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,securityContext);
            session.setAttribute("connectedUser",authenticatedUser);
            return "redirect:/";

        } catch (Exception e){
             return REDIRECT_LOGIN;
        }

    }

    @GetMapping(path="/logout")
    public String logout(SessionStatus sessionStatus, HttpServletRequest request, Model model){
        model.addAttribute("message","Logout Failed");
        HttpSession session = request.getSession();
        session.invalidate();
        sessionStatus.setComplete();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return "redirect:/home";
    }

    @GetMapping(path="/register")
    public String register(Model model)
    {
        User user = new User();
        model.addAttribute("user",user);
        model.addAttribute("message","Registration Failed");
        return "Auth/register";
    }

    @PostMapping(path="/register")
    public String createNewUser(RedirectAttributes redirectAttributes, @ModelAttribute("user") User user)
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        try {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);

            user.setRole("USER");
            User userTest = userRepository.findUserByEmail(user.getEmail());

            if(userTest != null){
                 return REDIRECT_REGISTER_ERROR;
            }

            User newUser = userService.createUser(user);
            if(newUser == null){
                 return REDIRECT_REGISTER_ERROR;
            }
            newUser.setProfilePicture("images/user/inconnu.jpg");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            return REDIRECT_LOGIN;

        } catch (Exception e){
            redirectAttributes.addAttribute("error", "Registred Failed"); // or some error message
            return REDIRECT_REGISTER_ERROR;
        }

    }
}
