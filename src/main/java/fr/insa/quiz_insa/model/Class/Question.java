package fr.insa.quiz_insa.model;

import jakarta.persistence.*;
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
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Question implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private String intitule;

    private String image;

    private int nb_correct;

    @ManyToOne
    @JoinColumn(name = "questionnaire")
    private Questionnaire questionnaire;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Choix> choix;

    @OneToOne(mappedBy = "question")
    private ReponseSimple reponseSimple;

    @OneToMany(mappedBy = "question_reponseQuiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ReponseQuiz> reponseQuizs;

    @Transient
    private String nb_correctError;
}