package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Class.Utilisateur;
import org.springframework.data.repository.CrudRepository;

public interface UtilisateurRepository extends CrudRepository<Utilisateur,Long> {

    Utilisateur findByEmail (String email);

    Iterable<Utilisateur> findByRole(String role);

}
