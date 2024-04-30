package ma.emsi.gestionnairedestaches.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("")
public class HomeController {

    @ResponseBody
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String index(){
        return "redirect:/userList";
    }

}
