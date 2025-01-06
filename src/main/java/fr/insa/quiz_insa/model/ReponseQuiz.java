package fr.insa.quiz_insa.model;


import jakarta.persistence.*;
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
public class ReponseQuiz implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private String reponse;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "note")
    private Note note;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "question")
    private Question question_reponseQuiz;

    public ReponseQuiz(String reponse, Note note, Question question_reponseQuiz) {
        this.reponse = reponse;
        this.note = note;
        this.question_reponseQuiz = question_reponseQuiz;
    }
}
