package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Class.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    public List<Question> findAll();

    public Question getQuestionById(long id);

    @Query("select q from Question q where q.questionnaire.id = :id")
    Iterable<Question> getQuestionByQuestionnaireId(Long questionnaireId);

    @Query("select q.questionnaire.id from Question q where q.id = :id")
    Long getQuestionnaireIdById(Long questionId);

    @Query("select q from Question q where q.questionnaire.id = :id and  q.id= :idQuestion")
    Question getQuestionByQuestionnaireIdAndId(Long idQuestionnaire, Long questionId);
}
