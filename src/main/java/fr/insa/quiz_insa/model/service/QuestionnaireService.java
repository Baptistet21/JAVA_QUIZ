package fr.insa.quiz_insa.model.service;

import fr.insa.quiz_insa.model.Class.Questionnaire;
import fr.insa.quiz_insa.repository.QuestionnaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionnaireService {

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    public Iterable<Questionnaire> findAll() {
        return questionnaireRepository.findAll();
    }

    public Questionnaire findById(Long id) {
        return questionnaireRepository.findById(id).orElse(null);
    }

    public Questionnaire save(Questionnaire questionnaire) {
        return questionnaireRepository.save(questionnaire);
    }

    public void deleteById(Long id) {
        questionnaireRepository.deleteById(id);
    }
}