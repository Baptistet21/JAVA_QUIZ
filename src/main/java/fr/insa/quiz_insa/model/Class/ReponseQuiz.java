package fr.insa.quiz_insa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReponseQuiz extends Question {

    @ManyToOne
    @JoinColumn(name = "note")
    private Note note;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "question")
    private Question question_reponseQuiz;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reponse")
    private ReponseSimple reponse;

    public ReponseQuiz(String reponse, Note note, Question question_reponseQuiz) {
        this.note = note;
        this.question_reponseQuiz = question_reponseQuiz;
    }
}