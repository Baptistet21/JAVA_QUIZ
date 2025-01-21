package fr.insa.quiz_insa.repository;


import fr.insa.quiz_insa.model.Class.ReponseSimple;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReponseSimpleRepository extends CrudRepository<ReponseSimple,Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM ReponseSimple r WHERE r.id = :id")
    void deleteReponseSimpleByQuestionId(Long id);


}