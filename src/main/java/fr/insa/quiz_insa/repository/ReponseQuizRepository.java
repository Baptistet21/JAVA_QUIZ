package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.ReponseQuiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReponseQuizRepository extends CrudRepository<ReponseQuiz, Long> {

    @Query("select r from ReponseQuiz r where r.note.id = :id")
    Iterable<ReponseQuiz> getReponseByNoteId(Long id);


}
