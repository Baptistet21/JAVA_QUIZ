package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UtilisateurRepository extends CrudRepository<Utilisateur,Long> {

    @Query("SELECT us FROM Utilisateur us WHERE us.email=:email")
    Optional<Utilisateur> findUserByEmail(@Param("email") String email);

}
