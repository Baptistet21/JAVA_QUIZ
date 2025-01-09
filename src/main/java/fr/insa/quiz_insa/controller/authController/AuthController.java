package fr.insa.quiz_insa.controller.authController;
import fr.insa.quiz_insa.model.Class.Utilisateur;
import fr.insa.quiz_insa.model.DTO.UserDTO;
import fr.insa.quiz_insa.model.security.LoginRequest;
import fr.insa.quiz_insa.model.service.UserService;
import fr.insa.quiz_insa.repository.UtilisateurRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    private UtilisateurRepository utilisateurRepository;

    public AuthController(UserService userService, UtilisateurRepository utilisateurRepository) {
        this.userService = userService;
        this.utilisateurRepository = utilisateurRepository;
    }


    /* renvoie la page html de connexion*/
    @GetMapping("/connexion")
    public String getLogin(Model model){
        model.addAttribute("req", new LoginRequest());
        return "auth/connexion";
    }

    /* renvoie la page html de inscription*/
    @GetMapping("/inscription")
    public String inscription(Model model) {
        model.addAttribute("utilisateur", new UserDTO());
        return "auth/inscription" ;
    }

    @PostMapping("/registration")
    public String registration(
            @ModelAttribute("utilisateur") @Valid UserDTO userDTO, BindingResult bindingResult,
            Model model
    ) throws IllegalArgumentException {
        Utilisateur existingEmail = utilisateurRepository.findByEmail(userDTO.getEmail());


        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "auth/inscription" ;
        }

        /* verification email */
        if (existingEmail != null) {
            userDTO.setEmailError("Un compte existe déjà avec cet email.");
            return "auth/inscription" ;
        }
        /* verification password */
        if (!(userDTO.getPassword().equals(userDTO.getConfirmPassword()))) {
            userDTO.setPasswordError("Les mots de passe ne correspondent pas.");
            return "auth/inscription" ;
        }

        userService.saveUser(userDTO);
        /* si inscription est correct renvoie vers page connexion */
        model.addAttribute("req", new LoginRequest());
        return "auth/connexion";
    }



}
