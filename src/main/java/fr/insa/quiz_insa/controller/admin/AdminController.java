package fr.insa.quiz_insa.controller.admin;

import fr.insa.quiz_insa.model.Class.*;
import fr.insa.quiz_insa.repository.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private QuestionnaireRepository questionnaireRepository;
    private QuestionRepository questionRepository;
    private ChoixRepository choixRepository;
    private UtilisateurRepository utilisateurRepository;
    private PossibiliteRepository possibiliteRepository;
    private ReponseSimpleRepository reponseSimpleRepository;
    private ReponseQuizRepository reponseQuizRepository;

    public AdminController(QuestionnaireRepository questionnaireRepository, QuestionRepository questionRepository,
                                   ChoixRepository choixRepository, UtilisateurRepository utilisateurRepository,
                           PossibiliteRepository possibiliteRepository, ReponseSimpleRepository reponseSimpleRepository,
                                   ReponseQuizRepository reponseQuizRepository) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionRepository = questionRepository;
        this.choixRepository = choixRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.possibiliteRepository = possibiliteRepository;
        this.reponseSimpleRepository = reponseSimpleRepository;
        this.reponseQuizRepository = reponseQuizRepository;

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

    /* QUESTIONNAIRE */
    /* renvoie la page de la creation du questionnaire, avec un nouvel questionnaire afin de le créer via le formulaire */

    @GetMapping("/questionnaire")
    public String createQuestionnaire(Model model) {
        model.addAttribute("questionnaire", new Questionnaire());
        return "admin/create_questionnaire";
    }

    /* renvoie la page de la creation de question, avec une nouvelle question afin de le créer via le formulaire et egalement l'id du formulaire correspondant */
    @GetMapping("/question/{questionnaireId}")
    public String createQuestion(@PathVariable Long questionnaireId, Model model) {
        model.addAttribute("questionnaireId", questionnaireId);
        model.addAttribute("question", new Question());
        return "admin/create_question";

    }
    /* permet de creer un questionnaire */
    @PostMapping("/creation_questionnaire")
    public String creationQuestionnaire(
            @ModelAttribute("questionnaire") @Valid Questionnaire questionnaire,
            BindingResult bindingResult,
            Model model
    ) throws IllegalArgumentException {

        // Recherche du questionnaire associé
        Optional<Questionnaire> questionnaireOp = questionnaireRepository.findQuestionnaireByTheme(questionnaire.getTheme());

        if (questionnaireOp.isPresent()) {
            questionnaire.setNiveauError("Ce questionnaire existe déjà");
            return "admin/create_questionnaire";
        }
        else {
            if (bindingResult.hasErrors()) {
                model.addAttribute("errors", bindingResult.getAllErrors());
                return "admin/create_questionnaire";
            }
            /* gestion erreur niveau */
            if (questionnaire.getNiveau() > 5 || questionnaire.getNiveau() < 1) {
                questionnaire.setNiveauError("Le niveau doit être compris entre 5 et 1 ");
                return "admin/create_questionnaire";

            }
            questionnaireRepository.save(questionnaire);
            Long questionnaireId = questionnaire.getId();

            /* renvoie a la page de creation de question */
            return "redirect:/admin/question/" + questionnaireId;
        }

    }

    /* creation du choix */
    public Possibilite createPossibilite(String nom,String rep,Choix choix){
        if(Objects.equals(rep, "on")){
            /* si il est selectionné correct */
            return new Possibilite(nom,true,choix);
        }
        else {
            /* si il est selectionné incorrect */
            return new Possibilite(nom,false,choix);
        }
    }

    public int verif_nb_correct(String rep){
        /* renvoie 1 si le choix est selectionné correct sinon 0 */
        if(Objects.equals(rep, "on")){
            return 1;
        }
        else {
            return 0;
        }
    }

    /* permet la creation de question, avec un intitule, un nombre de reponse correct, et 4 choix (un nom, un boolean si la reponse est correct */
    @PostMapping("/creationQuestion")
    public String createQuestion(@ModelAttribute("question") Question question,
                                 @RequestParam Long questionnaireId,
                                 @RequestParam String nom1, @RequestParam(required = false) String reponse1,
                                 @RequestParam String nom2, @RequestParam(required = false) String reponse2,
                                 @RequestParam String nom3, @RequestParam(required = false) String reponse3,
                                 @RequestParam String nom4, @RequestParam(required = false) String reponse4,
                                 RedirectAttributes redirectAttributes) {

        // Recherche du questionnaire associé
        Optional<Questionnaire> questionnaireOp = questionnaireRepository.findById(questionnaireId);

        if (questionnaireOp.isPresent()) {
            Questionnaire questionnaire = questionnaireOp.get();
            question.setQuestionnaire(questionnaire);

            // Calcul du nombre de réponses correctes sélectionnées
            int nbCorrectSelectionne = verif_nb_correct(reponse1)
                    + verif_nb_correct(reponse2)
                    + verif_nb_correct(reponse3)
                    + verif_nb_correct(reponse4);

            // Vérification du nombre de réponses correctes
            if (nbCorrectSelectionne == question.getNb_correct() && question.getNb_correct() > 0) {

                // Création de l'entité Choix
                Choix choix = new Choix(question.getIntitule(),question.getNb_correct(),questionnaire);
                choix.setQuestionnaire(questionnaire);
                choixRepository.save(choix);

                // Création et sauvegarde des possibilités associées
                Possibilite p1 = createPossibilite(nom1, reponse1, choix);
                Possibilite p2 = createPossibilite(nom2, reponse2, choix);
                Possibilite p3 = createPossibilite(nom3, reponse3, choix);
                Possibilite p4 = createPossibilite(nom4, reponse4, choix);

                possibiliteRepository.save(p1);
                possibiliteRepository.save(p2);
                possibiliteRepository.save(p3);
                possibiliteRepository.save(p4);

                // Redirection vers la page de gestion des questions
                return "redirect:/admin/question/" + questionnaireId;

            } else {
                // Gestion de l'erreur : nombre de réponses correctes incorrect
                redirectAttributes.addFlashAttribute("error", "Le nombre de réponses correctes sélectionnées est incorrect !");
                return "redirect:/admin/question/" + questionnaireId;
            }

        } else {
            // Gestion de l'erreur : questionnaire introuvable
            redirectAttributes.addFlashAttribute("error", "Le questionnaire spécifié est introuvable !");
            return "redirect:/admin/questionnaire";
        }
    }



    @PostMapping("/creation_questionLibre")
    public String createQuestionLibre(@ModelAttribute("question") Question question,
                                      @RequestParam Long questionnaireId,
                                      @RequestParam("repLibre") String repLibre) {

        // Récupération du questionnaire à partir de l'identifiant
        Optional<Questionnaire> questionnaireOp = questionnaireRepository.findById(questionnaireId);

        if (questionnaireOp.isPresent()) {
            Questionnaire questionnaire = questionnaireOp.get();

            // Associer le questionnaire à la question
            question.setQuestionnaire(questionnaire);

            // Définir le nombre de réponses correctes (1 pour une question libre)
            question.setNb_correct(1);

            // Créer une réponse simple avec les données correspondantes
            ReponseSimple reponseSimple = new ReponseSimple(
                    question.getIntitule(),  // Intitulé de la question
                    1,
                    questionnaire,
                    repLibre                // Texte de la réponse libre
            );

            // Sauvegarder la réponse simple
            reponseSimpleRepository.save(reponseSimple);

            // Rediriger vers la page des questions du questionnaire
            return "redirect:/admin/question/" + questionnaireId;
        } else {
            // Si le questionnaire n'existe pas, rediriger vers la liste des questionnaires
            return "redirect:/admin/questionnaire";
        }
    }

    /* SUPPRESSION QUESTIONNAIRE*/

    /* renvoie vers la page de suppression des quiz, avec en attribut la liste de tous les quiz*/
    @GetMapping("/menuSuppQuestionnaire")
    public String suppQuestionnaire(Model model) {
        Iterable<Questionnaire> questionnaires = questionnaireRepository.findAll();
        model.addAttribute("questionnaire", questionnaires);
        return "admin/supp_questionnaire";
    }

    /* supprime le quiz selon son id*/
    @Transactional
    @PostMapping("/suppQuestionnaire/{questionnaireId}")
    public String suppQuestionnaire(@PathVariable Long questionnaireId) {

        Iterable<Question> lesQuestions = questionRepository.getQuestionByQuestionnaireId(questionnaireId);

        questionnaireRepository.deleteById(questionnaireId);

        for (Question q : lesQuestions) {
            reponseSimpleRepository.deleteReponseSimpleByQuestionId(q.getId());

            possibiliteRepository.deletePossibiliteByChoix(q.getId());

            questionRepository.delete(q);
        }


        return "redirect:/admin/menuSuppQuestionnaire";
    }

    /* SUPPRESSION USER */

    /* renvoie vers la page de suppression d'un user, avec la liste des user en attribut */
    @GetMapping("/menuSuppUser")
    public String suppUser(Model model) {
        Iterable<Utilisateur> utilisateurs = utilisateurRepository.findByRole("USER");
        model.addAttribute("utilisateur", utilisateurs);
        return "admin/Supprimer_user";
    }
    /* SUPPRESSION USER */

    /* supprime l'utilisateur */
    @PostMapping("/suppUser/{userId}")
    public String suppUser(
            @PathVariable Long userId){

        utilisateurRepository.deleteById(userId);

        return "redirect:/admin/menuSuppUser";
    }



}