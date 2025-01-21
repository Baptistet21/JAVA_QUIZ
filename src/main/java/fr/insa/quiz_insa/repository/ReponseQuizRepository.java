package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Class.ReponseQuiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReponseQuizRepository extends CrudRepository<ReponseQuiz, Long> {
}
