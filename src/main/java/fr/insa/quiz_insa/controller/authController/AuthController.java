package fr.insa.quiz_insa.controller.authController;

import fr.insa.quiz_insa.model.DTO.UserDTO;
import fr.insa.quiz_insa.model.security.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    /* renvoie la page html de connexion*/
    @GetMapping("/connexion")
    public String getLogin(Model model){
        model.addAttribute("req", new LoginRequest());
        return "view/auth/connexion";
    }

    /* renvoie la page html de inscription*/
    @GetMapping("/inscription")
    public String inscription(Model model) {
        model.addAttribute("utilisateur", new UserDTO());
        return "view/auth/inscription" ;
    }


}
