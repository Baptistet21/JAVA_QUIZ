package fr.insa.quiz_insa.model.Class;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReponseSimple extends Question {
    @NotBlank
    private String texte;

    public ReponseSimple(String intitule,int nb_correct, Questionnaire questionnaire, String texte) {
        super(intitule, nb_correct, questionnaire);
        this.texte = texte;
    }

    public String getType() {
        return "ReponseSimple";
    }
}