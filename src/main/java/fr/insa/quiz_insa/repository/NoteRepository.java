package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Class.Note;
import fr.insa.quiz_insa.model.Class.Questionnaire;
import fr.insa.quiz_insa.model.Class.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface NoteRepository extends CrudRepository<Note,Long> {
    @Query("SELECT n FROM Note n WHERE n.utilisateur_note.id = :currentUserId")
    Iterable<Note> findAllByUtilisateur_noteId(Long currentUserId);
}
