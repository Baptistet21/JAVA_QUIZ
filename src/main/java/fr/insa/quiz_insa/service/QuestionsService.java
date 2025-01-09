package fr.insa.quiz_insa.service;

import fr.insa.quiz_insa.model.Class.Question;
import fr.insa.quiz_insa.repository.QuestionRepository;
import fr.insa.quiz_insa.repository.UtilisateurRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class QuestionsService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(long id) {
        return questionRepository.getQuestionById(id);
    }
}
