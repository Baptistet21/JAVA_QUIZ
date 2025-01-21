package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Class.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    public List<Question> findAll();

    public Question getQuestionById(long id);

    @Query("select q from Question q where q.questionnaire.id = :id")
    Iterable<Question> getQuestionByQuestionnaireId(@Param("id") Long id);

}
