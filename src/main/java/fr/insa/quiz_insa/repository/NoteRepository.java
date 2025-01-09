package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Note;
import fr.insa.quiz_insa.model.Questionnaire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface NoteRepository extends CrudRepository<Note,Long> {


    @Query("select distinct n.questionnaire_note from Note n where n.utilisateur_note.id = :id")
    Iterable<Questionnaire> getNoteByUserId(@Param("id") Long id);

    @Query("select n from Note n where n.utilisateur_note.id = :idUser and n.questionnaire_note.id = :idQuestionnaire")
    Iterable<Note> getNoteByUserIdAAndQuestionnaireId(@Param("idUser") Long idUser, @Param("idQuestionnaire") Long idQuestionnaire);

    @Query("select n from Note n where n.utilisateur_note.id = :idUser")
    Iterable<Note> getNoteByUser(@Param("idUser") Long idUser);


    @Query("select distinct n.questionnaire_note from Note n where n.id = :id")
    Questionnaire getQuestionnaireByNoteId(@Param("id") Long id);

}
