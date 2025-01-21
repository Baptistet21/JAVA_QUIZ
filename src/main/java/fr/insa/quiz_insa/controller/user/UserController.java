package fr.insa.quiz_insa.controller.user;

import fr.insa.quiz_insa.model.Class.*;
import fr.insa.quiz_insa.repository.NoteRepository;
import fr.insa.quiz_insa.repository.ReponseSimpleRepository;
import fr.insa.quiz_insa.repository.UtilisateurRepository;
import fr.insa.quiz_insa.service.QuestionService;
import fr.insa.quiz_insa.model.service.QuestionnaireService;
import fr.insa.quiz_insa.service.ReponseQuizService;
import fr.insa.quiz_insa.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private ReponseQuizService reponseQuizService;
    @Autowired
    private ReponseSimpleRepository reponseSimpleRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;


    @GetMapping("/home")
    public String getAllQuestionnaires(Model model) {
        Iterable<Questionnaire> questionnaires = questionnaireService.findAll();
        model.addAttribute("questionnaires", questionnaires);
        model.addAttribute("user", utilisateurService.getCurrentUser());
        return "user/home";
    }

    @GetMapping("/createQuestionnaire")
    public String showCreateForm(Model model) {
        model.addAttribute("questionnaire", new Questionnaire());
        return "user/createQuestionnaire";
    }

    @PostMapping("/createQuestionnaire")
    public String createQuestionnaire(@ModelAttribute Questionnaire questionnaire) {
        questionnaireService.save(questionnaire);
        return "redirect:/user/home";
    }

    @GetMapping("/addQuestion/{questionnaireId}")
    public String showAddQuestionForm(@PathVariable Long questionnaireId, Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("questionnaireId", questionnaireId);
        return "user/ajoutQuestion";
    }

    @PostMapping("/addQuestion/{questionnaireId}")
    public String addQuestion(@PathVariable Long questionnaireId, @ModelAttribute Question question) {
        Questionnaire questionnaire = questionnaireService.findById(questionnaireId);
        question.setQuestionnaire(questionnaire);
        questionService.save(question);
        return "redirect:/user/home";
    }

    @GetMapping("/questionnaire/{id}")
    public String createQuestionnaire(@PathVariable Long id, Model model) {
        Questionnaire questionnaire = questionnaireService.findById(id);
        model.addAttribute("questionnaire", questionnaire);

        Utilisateur currentUser = utilisateurService.getCurrentUser();

        System.out.println("Utilisateur : " + currentUser);
        model.addAttribute("user", currentUser);

        return "user/questionnaire";
    }

    @PostMapping("/submitQuestionnaire")
    public String submitQuestionnaire(@RequestParam Long userId, @RequestParam Map<String, String> answers, Model model) {
        // Retrieve the user by ID
        Utilisateur utilisateur = utilisateurService.findById(userId).orElse(null);
        if (utilisateur == null) {
            // Handle the case where the user is not found
            return "redirect:/user/home?error=userNotFound";
        }

        // Process each answer
        for (Map.Entry<String, String> entry : answers.entrySet()) {
            try {
                String key = entry.getKey();
                if (key.startsWith("answers[") && key.endsWith("]")) {
                    Long questionId = Long.parseLong(key.substring(8, key.length() - 1));
                    String answerContent = entry.getValue();

                    // Retrieve the question by ID
                    Question question = questionService.findById(questionId);
                    if (question != null) {
                        Optional<ReponseSimple> reponseSimple = reponseSimpleRepository.findById(questionId);
                        ReponseQuiz reponseQuiz = new ReponseQuiz();
                        reponseQuiz.setReponse(answerContent);
                        reponseQuiz.setUtilisateur(utilisateur);
                        reponseQuiz.setQuestion_reponseQuiz(question);

                        if (reponseSimple.isPresent()) {
                            if (reponseSimple.get().getTexte().equals(answerContent)) {
                                Note note = noteRepository.save(new Note(1, LocalDateTime.now(), utilisateur, question.getQuestionnaire()));
                                reponseQuiz.setNote(note);
                            } else {
                                Note note = noteRepository.save(new Note(0, LocalDateTime.now(), utilisateur, question.getQuestionnaire()));
                                reponseQuiz.setNote(note);
                            }
                        }

                        reponseQuizService.save(reponseQuiz);
                    }
                }
            } catch (NumberFormatException e) {
                // Handle the case where the question ID is not a valid number
                e.printStackTrace();
            }
        }

        // Redirect to a confirmation page or back to the questionnaire list
        return "redirect:/user/home";
    }


    @GetMapping("/resultats")
    public String showResults(Model model) {
        Utilisateur currentUser = utilisateurService.getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("notes", noteRepository.findAllByUtilisateur_noteId(currentUser.getId()));
        return "user/tableau_note";
    }

    /* redirection vers profil */
    @GetMapping("/profil")
    public String profil(Model model) {
        Utilisateur currentUser = utilisateurService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
            return "user/profil";
        } else {
            return "redirect:/user/home";
        }
    }

    /* méthode qui supprime un compte */
    @PostMapping("/delete")
    public String suppUser(
            @PathVariable Long userId){


        utilisateurRepository.deleteById(userId);

        Iterable<Note> lesNotes = noteRepository.findAllByUtilisateur_noteId(userId);

        for (Note n : lesNotes){
            noteRepository.deleteById(n.getId());
        }

        return "redirect:/auth/connexion";
    }
}