package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Class.Choix;
import fr.insa.quiz_insa.model.Class.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository

public interface ChoixRepository extends CrudRepository<Choix,Long> {

}