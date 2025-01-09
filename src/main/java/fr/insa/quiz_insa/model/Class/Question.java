package fr.insa.quiz_insa.model.Class;

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
public class Question implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    private String intitule;

    private String image;

    private int nb_correct;

    @ManyToOne
    @JoinColumn(name = "questionnaire")
    private Questionnaire questionnaire;

    @Transient
    private String nb_correctError;
}