package fr.insa.quiz_insa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Utilisateur implements Serializable {

    /* attributs d'un utilisateur */
    @Id
    @GeneratedValue /* generation auto de l'id*/
    private long id;

    @NotBlank
    @Email /* verifie si l'attribut est un email*/
    @Column(unique = true)
    private String email;

    @NotBlank /* ne peut pas etre un champ vide */
    private String nom;


    @NotBlank
    private String prenom;

    @NotBlank
    @JsonIgnore
    private String password;

    @NotNull
    private LocalDate date_de_naissance;

    @NotBlank
    private String role;

    @OneToMany(mappedBy = "utilisateur_note", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Note> notes;

    @Column(name = "last_active")
    private LocalDateTime lastActive;

    public Utilisateur(String email, String nom, String prenom, String password, LocalDate date_de_naissance, LocalDateTime lastActive) {
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.date_de_naissance = date_de_naissance;
        this.lastActive = lastActive;
    }


}
