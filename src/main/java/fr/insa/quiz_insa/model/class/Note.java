package fr.insa.quiz_insa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Note implements Serializable {
    /* attribut d'un note */

    @Id @GeneratedValue
    private long id;

    private int resultat;

    private LocalDateTime date;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ReponseQuiz> reponseQuiz;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "utilisateur_note")
    private Utilisateur utilisateur_note;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "questionnaire_note")
    private Questionnaire questionnaire_note;


    public Note(int resultat, LocalDateTime date, Utilisateur utilisateur_note, Questionnaire questionnaire_note) {
        this.resultat = resultat;
        this.date = date;
        this.utilisateur_note = utilisateur_note;
        this.questionnaire_note = questionnaire_note;
    }
}
