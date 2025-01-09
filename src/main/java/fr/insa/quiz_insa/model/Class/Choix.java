package fr.insa.quiz_insa.model.Class;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Choix implements Serializable {
    /* attribut d'un choix */
    @Id @GeneratedValue
    private long id;

    @NotBlank
    private String nom;

    /* true si reponse est correct */
    private boolean reponse;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "question")
    private Question question;

    public Choix(String nom, boolean reponse, Question question) {
        this.nom = nom;
        this.reponse = reponse;
        this.question = question;
    }
}
