package fr.insa.quiz_insa.model.Class;

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
public class Possibilite implements Serializable {
    @Id @GeneratedValue
    private long id;

    @NotBlank
    private String name;

    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "choix_id")
    private Choix choix;

    public Possibilite(String name, boolean correct, Choix choix) {
        this.name = name;
        this.correct = correct;
        this.choix = choix;
    }
}