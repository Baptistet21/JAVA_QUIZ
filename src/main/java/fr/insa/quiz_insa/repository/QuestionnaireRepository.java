package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Class.Questionnaire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface QuestionnaireRepository extends CrudRepository<Questionnaire,Long> {

    Optional<Questionnaire> findQuestionnaireByTheme(String name);

}