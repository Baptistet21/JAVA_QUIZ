package fr.insa.quiz_insa.controller.admin;

import fr.insa.quiz_insa.model.Class.*;
import fr.insa.quiz_insa.repository.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private QuestionnaireRepository questionnaireRepository;
    private QuestionRepository questionRepository;
    private ChoixRepository choixRepository;
    private UtilisateurRepository utilisateurRepository;
    private NoteRepository noteRepository;
    private ReponseSimpleRepository reponseSimpleRepository;
    private ReponseQuizRepository reponseQuizRepository;

    public AdminController(QuestionnaireRepository questionnaireRepository, QuestionRepository questionRepository,
                                   ChoixRepository choixRepository, UtilisateurRepository utilisateurRepository,
                                   NoteRepository noteRepository, ReponseSimpleRepository reponseSimpleRepository,
                                   ReponseQuizRepository reponseQuizRepository) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionRepository = questionRepository;
        this.choixRepository = choixRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.noteRepository = noteRepository;
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
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "admin/create_questionnaire";
        }
        /* gestion erreur niveau */
        if(questionnaire.getNiveau() > 5 || questionnaire.getNiveau() < 1){
            questionnaire.setNiveauError("Le niveau doit être compris entre 5 et 1 ");
            return "admin/create_questionnaire";

        }
        questionnaireRepository.save(questionnaire);
        Long questionnaireId = questionnaire.getId();

        /* renvoie a la page de creation de question */
        return "redirect:/admin/question/" + questionnaireId;
    }

    /* creation du choix */
    public Choix createChoix(String nom,String rep,Question question){
        if(Objects.equals(rep, "on")){
            /* si il est selectionné correct */
            return new Choix(nom,true,question);
        }
        else {
            /* si il est selectionné incorrect */
            return new Choix(nom,false,question);
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
//
//    /* permet la creation de question, avec un intitule, un nombre de reponse correct, et 4 choix (un nom, un boolean si la reponse est correct */
//    @PostMapping("/creationQuestion")
//    public String createQuestion(@ModelAttribute("question") Question question,
//                                 @RequestParam Long questionnaireId,
//                                 @RequestParam("imageUpload") MultipartFile imageUpload,
//                                 @RequestParam String nom1, @RequestParam(required = false) String reponse1,
//                                 @RequestParam String nom2, @RequestParam(required = false) String reponse2,
//                                 @RequestParam String nom3, @RequestParam(required = false) String reponse3,
//                                 @RequestParam String nom4, @RequestParam(required = false) String reponse4, RedirectAttributes redirectAttributes) {
//
//        Optional<Questionnaire> questionnaireOp = questionnaireRepository.findById(questionnaireId);
//
//
//        /* calcul du nombre de correct selectionné*/
//        int nb_correct = verif_nb_correct(reponse1) + verif_nb_correct(reponse2) + verif_nb_correct(reponse3) + verif_nb_correct(reponse4);
//
//        if (nb_correct == question.getNb_correct() && question.getNb_correct() > 0) {
//            if (questionnaireOp.isPresent()) {
//                Questionnaire questionnaire = questionnaireOp.get();
//                question.setQuestionnaire(questionnaire);
//
//                /* sauvegarde de la question*/
//                questionRepository.save(question);
//
//                /* sauvegarde des choix par rapport a une question */
//                Choix choix1 = createChoix(nom1, reponse1, question);
//                Choix choix2 = createChoix(nom2, reponse2, question);
//                Choix choix3 = createChoix(nom3, reponse3, question);
//                Choix choix4 = createChoix(nom4, reponse4, question);
//
//                choixRepository.save(choix1);
//                choixRepository.save(choix2);
//                choixRepository.save(choix3);
//                choixRepository.save(choix4);
//
//
//                return "redirect:/admin/question/" + questionnaireId;
//            }
//            else {
//                return "redirect:/admin/questionnaire";
//            }
//        }
//        else {
//            /* gestion erreur si le nombre de correct n'est pas egal au nombre de correct selectionné */
//            redirectAttributes.addAttribute("error", "Vous n'avez pas indiqué le bon nombre de réponses correctes !");
//            return "redirect:/admin/question/" + questionnaireId;
//
//        }
//
//    }
//
//    @PostMapping("/creationQuestionLibre")
//    public String createQuestionLibre(@ModelAttribute("question") Question question,
//                                      @RequestParam Long questionnaireId,
//                                      @RequestParam("repLibre") String repLibre)  {
//
//        Optional<Questionnaire> questionnaireOp = questionnaireRepository.findById(questionnaireId);
//
//        if (questionnaireOp.isPresent()) {
//            Questionnaire questionnaire = questionnaireOp.get();
//            question.setQuestionnaire(questionnaire);
//
//            question.setNb_correct(1);
//
//            questionRepository.save(question);
//
//            ReponseSimple reponseSimple = new ReponseSimple(repLibre, question);
//            reponseSimpleRepository.save(reponseSimple);
//
//
//            return "redirect:/admin/question/" + questionnaireId;
//        } else {
//            return "redirect:/admin/questionnaire";
//        }
//    }

}