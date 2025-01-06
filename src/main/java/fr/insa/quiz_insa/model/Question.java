package fr.insa.quiz_insa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Question implements Serializable {
    /* attribut d'une question */
    @Id @GeneratedValue
    private long id;

    @NotBlank
    private String intitule;

    private String image;

    private int nb_correct;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "questionnaire")
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(min = 4, max = 4) /* doit obligatoirement avoir 4 choix */
    @JsonIgnore
    private Set<Choix> choix;

    @OneToOne(mappedBy = "question")
    private ReponseSimple reponseSimple;

    @OneToMany(mappedBy = "question_reponseQuiz", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ReponseQuiz> reponseQuizs;

    @Transient
    private String nb_correctError;

}
