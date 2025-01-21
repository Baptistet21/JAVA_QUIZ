package fr.insa.quiz_insa.model.Class;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Choix extends Question implements Serializable {

    @OneToMany(mappedBy = "choix", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Possibilite> possibilites;

    public Choix(String intitule, int nb_correct, Questionnaire questionnaire) {
        super(intitule, nb_correct, questionnaire);
    }

    public String getType() {
        return "Choix";
    }
}
