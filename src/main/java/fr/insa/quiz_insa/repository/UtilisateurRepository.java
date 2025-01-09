package fr.insa.quiz_insa.repository;

import fr.insa.quiz_insa.model.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



    @Repository
    public interface UtilisateurRepository extends CrudRepository<Utilisateur,Long> {

        Utilisateur findByEmail (String email);

        @Query("SELECT us FROM Utilisateur us WHERE us.email=:email")
        Optional<Utilisateur> findUserByEmail(@Param("email") String email);


        Iterable<Utilisateur> findByRole(String role);

        List<Utilisateur> findByLastActiveBefore(LocalDateTime date);

        @Query("SELECT u FROM Utilisateur u " +
                "WHERE " +
                "(u.email LIKE %:filtre% OR " +
                "u.nom LIKE %:filtre% OR " +
                "u.prenom LIKE %:filtre%) AND u.role = 'USER'")
        Iterable<Utilisateur> findByFiltre(String filtre);




}
