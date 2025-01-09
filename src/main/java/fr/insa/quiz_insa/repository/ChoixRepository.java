package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Choix;
import fr.insa.quiz_insa.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository

public interface ChoixRepository extends CrudRepository<Choix,Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Choix c WHERE c.question = :question")
    void deleteChoixByQuestion(Question question);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Choix c WHERE c.question.id = :id")
    void deleteChoixByQuestionId(Long id);


    @Query("select c.id from Choix c where c.question.questionnaire.id = :id and c.reponse = true")
    List<Long> getChoixTrue(@Param("id") Long id);

}