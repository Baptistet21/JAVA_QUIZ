package fr.insa.quiz_insa.controller.admin;


import fr.insa.quiz_insa.model.Class.*;
import fr.insa.quiz_insa.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class QuestionAdminController {
    private QuestionnaireRepository questionnaireRepository;
    private QuestionRepository questionRepository;
    private ChoixRepository choixRepository;
    private UtilisateurRepository utilisateurRepository;
    private NoteRepository noteRepository;

    private ReponseSimpleRepository reponseSimpleRepository;
    private ReponseQuizRepository reponseQuizRepository;


    @Value("${file.upload-dir-image}")
    private String uploadDirImage;
    private static final Logger logger = LogManager.getLogger(QuestionAdminController.class);

    private static final String MENU_VIEW = "admin/menuAdmin";
    private static final String CREATE_QUESTIONNAIRE_VIEW = "admin/creationQuestionnaire";
    private static final String QUESTION_VIEW = "redirect:/admin/question/";

    private static final String QUESTIONNAIRE = "questionnaire";

    public QuestionAdminController(QuestionnaireRepository questionnaireRepository, QuestionRepository questionRepository,
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
        return MENU_VIEW;
    }



    /* QUESTIONNAIRE */
    /* renvoie la page de la creation du questionnaire, avec un nouvel questionnaire afin de le créer via le formulaire */

    @GetMapping("/questionnaire")
    public String createQuestionnaire(Model model) {
        model.addAttribute(QUESTIONNAIRE, new Questionnaire());
        return CREATE_QUESTIONNAIRE_VIEW;
    }

    /* renvoie la page de la creation de question, avec une nouvelle question afin de le créer via le formulaire et egalement l'id du formulaire correspondant */
    @GetMapping("/question/{questionnaireId}")
    public String createQuestion(@PathVariable Long questionnaireId, Model model) {
        model.addAttribute("questionnaireId", questionnaireId);
        model.addAttribute("question", new Question());
        return "admin/createQuestion";

    }

    /* permet de creer un questionnaire */
    @PostMapping("/creationQuestionnaire")
    public String creationQuestionnaire(
            @ModelAttribute("questionnaire") @Valid Questionnaire questionnaire, @RequestParam String password,
            BindingResult bindingResult,
            Model model
    ) throws IllegalArgumentException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return CREATE_QUESTIONNAIRE_VIEW;
        }
        /* gestion erreur niveau */
        if(questionnaire.getNiveau() > 5 || questionnaire.getNiveau() < 1){
            questionnaire.setNiveauError("Le niveau doit être compris entre 5 et 1 ");
            return CREATE_QUESTIONNAIRE_VIEW;

        }
        if (password.equals("")){
            questionnaire.setPassword(null);
        }
        else {
            questionnaire.setPassword(password);
        }
        questionnaireRepository.save(questionnaire);
        Long questionnaireId = questionnaire.getId();

        /* renvoie a la page de creation de question */
        return QUESTION_VIEW + questionnaireId;

    }




    /* permet la creation de question, avec un intitule, un nombre de reponse correct, et 4 choix (un nom, un boolean si la reponse est correct */
    @PostMapping("/creationQuestion")
    public String createQuestion(@ModelAttribute("question") Question question,
                                 @RequestParam Long questionnaireId,
                                 @RequestParam("imageUpload") MultipartFile imageUpload,
                                 @RequestParam String nom1, @RequestParam(required = false) String reponse1,
                                 @RequestParam String nom2, @RequestParam(required = false) String reponse2,
                                 @RequestParam String nom3, @RequestParam(required = false) String reponse3,
                                 @RequestParam String nom4, @RequestParam(required = false) String reponse4, RedirectAttributes redirectAttributes) {

        Optional<Questionnaire> questionnaireOp = questionnaireRepository.findById(questionnaireId);


        /* calcul du nombre de correct selectionné*/
        int nb_correct = verif_nb_correct(reponse1) + verif_nb_correct(reponse2) + verif_nb_correct(reponse3) + verif_nb_correct(reponse4);

        if (nb_correct == question.getNb_correct() && question.getNb_correct() > 0) {
            if (questionnaireOp.isPresent()) {
                Questionnaire questionnaire = questionnaireOp.get();
                question.setQuestionnaire(questionnaire);

                if (!(imageUpload.isEmpty())) {
                    /* enregistrer image*/
                    question.setImage(isEmpty(imageUpload,questionnaireId));

                }

                /* sauvegarde de la question*/
                questionRepository.save(question);

                /* sauvegarde des choix par rapport a une question */
                Choix choix1 = createChoix(nom1, reponse1, question);
                Choix choix2 = createChoix(nom2, reponse2, question);
                Choix choix3 = createChoix(nom3, reponse3, question);
                Choix choix4 = createChoix(nom4, reponse4, question);

                choixRepository.save(choix1);
                choixRepository.save(choix2);
                choixRepository.save(choix3);
                choixRepository.save(choix4);


                return QUESTION_VIEW + questionnaireId;
            }
            else {
                return "redirect:/admin/questionnaire";
            }
        }
        else {
            /* gestion erreur si le nombre de correct n'est pas egal au nombre de correct selectionné */
            redirectAttributes.addAttribute("error", "Vous n'avez pas indiqué le bon nombre de réponses correctes !");
            return QUESTION_VIEW + questionnaireId;

        }

    }

    @PostMapping("/creationQuestionLibre")
    public String createQuestionLibre(@ModelAttribute("question") Question question,
                                      @RequestParam Long questionnaireId,
                                      @RequestParam("imageUploadForm2") MultipartFile imageUploadForm2,
                                      @RequestParam("repLibre") String repLibre)  {

        Optional<Questionnaire> questionnaireOp = questionnaireRepository.findById(questionnaireId);

        if (questionnaireOp.isPresent()) {
            Questionnaire questionnaire = questionnaireOp.get();
            question.setQuestionnaire(questionnaire);

            if (!imageUploadForm2.isEmpty()) {
                // Enregistrer l'image si elle est fournie
                String imagePath = isEmpty(imageUploadForm2, questionnaireId);
                question.setImage(imagePath);

            }

            question.setNb_correct(1);

            questionRepository.save(question);

            ReponseSimple reponseSimple = new ReponseSimple(repLibre, question.getId());
            reponseSimpleRepository.save(reponseSimple);


            return QUESTION_VIEW + questionnaireId;
        } else {
            return "redirect:/admin/questionnaire";
        }
    }


    public String isEmpty(MultipartFile object,long questionnaire_id){
        if (object.isEmpty()) {
            logger.error("Cette question n'a pas d'image");
        } else {
            try {
                String lien = System.currentTimeMillis() + "_" + questionnaire_id + "_" + object.getOriginalFilename();
                java.io.File dest = new java.io.File(uploadDirImage + java.io.File.separator + lien);
                object.transferTo(dest);
                return lien;
            } catch (IOException e) {
                logger.error("An error occurred during file transfer:", e);
            }
        }
        return null;
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

    /* SUPPRESSION QUESTIONNAIRE*/

    /* renvoie vers la page de suppression des quiz, avec en attribut la liste de tous les quiz*/
    @GetMapping("/menuSuppQuestionnaire")
    public String suppQuestionnaire(Model model) {
        Iterable<Questionnaire> questionnaires = questionnaireRepository.findAll();
        model.addAttribute(QUESTIONNAIRE, questionnaires);
        return "admin/SupprimerQuestionnaire";
    }
    @GetMapping("/menuSuppQuestionnaireByFiltre")
    public String suppQuestionnaireFiltre(Model model, @RequestParam("filtre") String filtre) {
        Iterable<Questionnaire> questionnaires = questionnaireRepository.findAll();
        if(!Objects.equals(filtre, "")){
            questionnaires = questionnaireRepository.findByFiltre(filtre);
        }
        model.addAttribute(QUESTIONNAIRE, questionnaires);
        return "admin/SupprimerQuestionnaire";
    }

    /* supprime le quiz selon son id*/
    @PostMapping("/suppQuestionnaire/{questionnaireId}")
    public String suppQuestionnaire(@PathVariable Long questionnaireId) {

        Iterable<Question> lesQuestions = questionRepository.getQuestionByQuestionnaireId(questionnaireId);

        for (Question q : lesQuestions) {
            // Supprime les réponses simples associées
            reponseSimpleRepository.deleteReponseSimpleByQuestionId(q.getId());

            // Supprime les choix associés
            choixRepository.deleteChoixByQuestion(q);

            // Supprime la question
            questionRepository.delete(q);
        }

        //supprime le questionnaire
        questionnaireRepository.deleteById(questionnaireId);

        return "redirect:/admin/menuSuppQuestionnaire";
    }


    /* SUPPRESSION QUESTION */

    /* renvoie vers la page de suppression des questions par rapport a leur questionnaire, avec en attribut la liste de tous les questions */
    @GetMapping("/menuSuppQuestion/{questionnaireId}")
    public String suppQuestion(@PathVariable Long questionnaireId,Model model) {
        Iterable<Question> questions = questionRepository.getQuestionByQuestionnaireId(questionnaireId);
        model.addAttribute("questions", questions);

        return "admin/supprimerQuestions";
    }

    /* supprime la question selon son id*/

    @PostMapping("/suppQuestion/{questionId}")
    public String suppQuestion(
            @PathVariable Long questionId){

        /* supprime la question */
        Long idQuestionnaire =questionRepository.getQuestionnaireIdById(questionId);
        Question question = questionRepository.getQuestionByQuestionnaireIdAndId(idQuestionnaire,questionId);
//        if (question.getReponseSimple() != null) {
//            /* supprime la question simple */
//            reponseSimpleRepository.deleteReponseSimpleByQuestionId(questionId);
//            questionRepository.deleteById(questionId);
//        }
//        else {
            /* supprime les choix */
            questionRepository.deleteById(questionId);
            choixRepository.deleteChoixByQuestionId(questionId);
       // }

        return "redirect:/admin/menuSuppQuestion/" + idQuestionnaire;
    }

    /* MODIFIER QUESTION */

    /* renvoie vers la page de modif des quiz, avec en attribut la liste de tous les quiz */
    @GetMapping("/menuModifQuestionnaire")
    public String menuModifQuestionnaire(Model model) {
        Iterable<Questionnaire> questionnaires = questionnaireRepository.findAll();
        model.addAttribute(QUESTIONNAIRE, questionnaires);
        return "admin/menuModifQuestionnaire";
    }

    @GetMapping("/menuModifQuestionnaireByFiltre")
    public String modifQuestionnaireFiltre(Model model, @RequestParam("filtre") String filtre) {
        Iterable<Questionnaire> questionnaires = questionnaireRepository.findAll();
        if(!Objects.equals(filtre, "")){
            questionnaires = questionnaireRepository.findByFiltre(filtre);
        }
        model.addAttribute(QUESTIONNAIRE, questionnaires);
        return "admin/menuModifQuestionnaire";
    }

    /* renvoie vers la page de modif des informations de quiz, avec en attribut l'id du quiz */
    @GetMapping("/modifQuestionnaire/{questionnaireId}")
    public String modifQuestionaire(@PathVariable Long questionnaireId,Model model) {
        Optional<Questionnaire> questionnaireOptional = questionnaireRepository.findById(questionnaireId);
        if (questionnaireOptional.isPresent()) {
            Questionnaire questionnaire = questionnaireOptional.get();
            model.addAttribute(QUESTIONNAIRE, questionnaire);
        } else {
            return MENU_VIEW;
        }
        return "admin/modifQuestionnaire";
    }

    /* modif le theme et/ou le niveau du quiz */
    @PostMapping("/postModifQuestionnaire/{questionnaireId}")
    public String postModifQuestionaire(@PathVariable Long questionnaireId,@RequestParam("theme") String theme, @RequestParam("niveau") int niveau,
                                        @RequestParam("password") String password) {
        Optional<Questionnaire> questionnaireOptional = questionnaireRepository.findById(questionnaireId);
        if (questionnaireOptional.isPresent()) {
            Questionnaire questionnaire = questionnaireOptional.get();
            /* modification des info du quiz */
            questionnaire.setTheme(theme);
            questionnaire.setNiveau(niveau);
            questionnaire.setPassword(password);
            questionnaireRepository.save(questionnaire);

        } else {
            return MENU_VIEW;
        }
        return "redirect:/admin/menuModifQuestionnaire";

    }

    /* SUPPRESSION USER */

    /* renvoie vers la page de suppression d'un user, avec la liste des user en attribut */
    @GetMapping("/menuSuppUser")
    public String suppUser(Model model) {
        Iterable<fr.insa.quiz_insa.model.Class.Utilisateur> utilisateurs = utilisateurRepository.findByRole("USER");
        model.addAttribute("utilisateur", utilisateurs);
        return "admin/SupprimerUser";
    }
    @GetMapping("/menuSuppUserByFiltre")
    public String suppUserFiltre(Model model, @RequestParam("filtre") String filtre) {
        Iterable<Utilisateur> utilisateurs = utilisateurRepository.findByRole("USER");
        if(!Objects.equals(filtre, "")){
            utilisateurs = utilisateurRepository.findByFiltre(filtre);
        }
        System.out.println(utilisateurs);

        model.addAttribute("utilisateur", utilisateurs);
        return "admin/SupprimerUser";
    }

    /* supprime l'utilisateur et ses notes */
    @PostMapping("/suppUser/{userId}")
    public String suppUser(
            @PathVariable Long userId){


        utilisateurRepository.deleteById(userId);

        Iterable<Note> lesNotes = noteRepository.getNoteByUser(userId);

        for (Note n : lesNotes){
            noteRepository.deleteById(n.getId());
        }

        return "redirect:/admin/menuSuppUser";
    }

    /* renvoie vers page de suppression des notes selon l'id d'un user */
    @GetMapping("/suppNote/{userId}")
    public String suppNote(@PathVariable Long userId,Model model) {
        Iterable<Note> lesNotes = noteRepository.getNoteByUser(userId);
        model.addAttribute("note", lesNotes);
        return "admin/suppNote";
    }

    /* supprime une note selon son id et l'id de l'utilisateur */
    @PostMapping("/postSuppNote/{userId}/{noteId}")
    public String postSuppNote(@PathVariable Long userId,@PathVariable Long noteId) {
        noteRepository.deleteById(noteId);
        return "redirect:/admin/suppNote/"+userId;
    }

    /* correction*/

    @GetMapping("/correction/{questionnaireId}")
    public String correction(@PathVariable Long questionnaireId, Model model) {
        Optional<Questionnaire> questionnaireOp = questionnaireRepository.findById(questionnaireId);
        if (questionnaireOp.isPresent()) {
            Questionnaire questionnaire = questionnaireOp.get();
            model.addAttribute(QUESTIONNAIRE, questionnaire);
            Iterable<Question> LesQuestions = questionRepository.getQuestionByQuestionnaireId(questionnaireId);
            model.addAttribute("question", LesQuestions);




        } else {
            return "redirect:MENU_VIEW";
        }

        return "admin/correctionAdmin";
    }


    /* revoir quiz des utilisateurs */

    @GetMapping("/revoirQuiz/{userId}/{noteId}")
    public String revoirQuiz(@PathVariable Long userId,@PathVariable Long noteId, Model model) {
        Optional<Utilisateur> userOp = utilisateurRepository.findById(userId);
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if (userOp.isPresent() && noteOptional.isPresent()) {
            Questionnaire questionnaire = noteRepository.getQuestionnaireByNoteId(noteId);
            Iterable<ReponseQuiz> reponseQuizs = reponseQuizRepository.getReponseByNoteId(noteId); /* liste des reponses d'un quiz fait */


            model.addAttribute("note", noteOptional.get());
            model.addAttribute("user", userOp.get());
            model.addAttribute("reponseQuiz", reponseQuizs);
            model.addAttribute(QUESTIONNAIRE, questionnaire);

            return "/admin/revoirQuizUser";



        } else {
            return "redirect:MENU_VIEW";
        }

    }

    /*Profil*/

    /* renvoie la page du profil */
    @GetMapping("/profil")
    public String profil(Model model) {
        Iterable<Utilisateur> utilisateurs = utilisateurRepository.findByRole("ADMIN");
        model.addAttribute("utilisateur", utilisateurs);
        return "admin/profil_admin";
    }

    /* méthode qui supprime un compte */

    @PostMapping("/suppCompte/{userId}")
    public String suppAdmin(
            @PathVariable Long userId){
        utilisateurRepository.deleteById(userId);
        return "redirect:/auth/connexion";
    }

}