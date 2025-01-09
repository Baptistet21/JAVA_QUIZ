package fr.insa.quiz_insa.controller.admin;

import fr.insa.quiz_insa.model.Class.Utilisateur;
import fr.insa.quiz_insa.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UtilisateurRepository utilisateurRepository;

    public AdminController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }


    /*MENU*/

    /* renvoie la page du menu admin */
    @GetMapping("/menu")
    public String menu() {
        return "admin/menu_admin";
    }

    /*Profil*/

    /* renvoie la page du profil */
    @GetMapping("/profil")
    public String profil(Model model) {
        Iterable<Utilisateur> utilisateurs = utilisateurRepository.findByRole("ADMIN");
        model.addAttribute("utilisateur", utilisateurs);
        return "admin/profil_admin";
    }


}