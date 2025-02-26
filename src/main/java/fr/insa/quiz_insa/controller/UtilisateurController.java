package fr.insa.quiz_insa.controller;

import fr.insa.quiz_insa.model.Class.Utilisateur;
import fr.insa.quiz_insa.service.QuestionService;
import fr.insa.quiz_insa.model.service.QuestionnaireService;
import fr.insa.quiz_insa.service.ReponseQuizService;
import fr.insa.quiz_insa.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private ReponseQuizService reponseQuizService;

    @GetMapping
    public Iterable<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        Optional<Utilisateur> utilisateur = utilisateurService.findById(id);
        return utilisateur.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Utilisateur createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.save(utilisateur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateurDetails) {
        Optional<Utilisateur> utilisateur = utilisateurService.findById(id);
        if (utilisateur.isPresent()) {
            Utilisateur updatedUtilisateur = utilisateur.get();
            updatedUtilisateur.setEmail(utilisateurDetails.getEmail());
            updatedUtilisateur.setNom(utilisateurDetails.getNom());
            updatedUtilisateur.setPrenom(utilisateurDetails.getPrenom());
            updatedUtilisateur.setPassword(utilisateurDetails.getPassword());
            updatedUtilisateur.setDate_de_naissance(utilisateurDetails.getDate_de_naissance());
            updatedUtilisateur.setRole(utilisateurDetails.getRole());
            updatedUtilisateur.setLastActive(utilisateurDetails.getLastActive());
            return ResponseEntity.ok(utilisateurService.save(updatedUtilisateur));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        if (utilisateurService.findById(id).isPresent()) {
            utilisateurService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}