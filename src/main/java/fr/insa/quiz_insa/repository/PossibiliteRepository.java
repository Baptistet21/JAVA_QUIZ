package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Class.Choix;
import fr.insa.quiz_insa.model.Class.Possibilite;
import fr.insa.quiz_insa.model.Class.Question;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PossibiliteRepository extends CrudRepository<Possibilite,Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Possibilite c WHERE c.choix.id = :choix")
    void deletePossibiliteByChoix(Long choix);}
