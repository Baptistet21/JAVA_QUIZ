package fr.insa.quiz_insa.service;

import fr.insa.quiz_insa.model.Class.ReponseQuiz;
import fr.insa.quiz_insa.repository.ReponseQuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReponseQuizService {
    @Autowired
    private ReponseQuizRepository reponseQuizRepository;

    public void save(ReponseQuiz reponseQuiz) {
        reponseQuizRepository.save(reponseQuiz);
    }
}
