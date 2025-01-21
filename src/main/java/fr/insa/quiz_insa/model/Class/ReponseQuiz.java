package fr.insa.quiz_insa.model.Class;

import jakarta.persistence.*;
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
public class ReponseQuiz {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "note")
    private Note note;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "utilisateur")
    private Utilisateur utilisateur;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "question")
    private Question question_reponseQuiz;

    @NotNull
    private String reponse;

    public ReponseQuiz(String reponse, Note note, Question question_reponseQuiz) {
        this.note = note;
        this.question_reponseQuiz = question_reponseQuiz;
    }

}