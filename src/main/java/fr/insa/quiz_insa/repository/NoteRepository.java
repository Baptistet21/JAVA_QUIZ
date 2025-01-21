package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Class.Note;
import fr.insa.quiz_insa.model.Class.Questionnaire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface NoteRepository extends CrudRepository<Note,Long> {
}
