package fr.insa.quiz_insa.model.Class;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class ReponseSimple implements Serializable  {

    @Id
    @GeneratedValue
    private long id;

    @NotBlank
    private String texte;


    @OneToOne
    @JoinColumn(name="question_id")
    @JsonIgnore
    private Question question;


    public ReponseSimple(String texte, Question question) {
        this.texte = texte;
        this.question = question;
    }
}
