package fr.insa.quiz_insa.controller;

import fr.insa.quiz_insa.service.QuestionsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuestionController {
    private QuestionsService questionService;

    public QuestionController(QuestionsService questionsService) {
        this.questionService = questionsService;
    }

    @GetMapping("/quiz")
    public String quiz(Model model) {
        model.addAttribute("questions", questionService.getQuestions());
        return "quiz";
    }
}
