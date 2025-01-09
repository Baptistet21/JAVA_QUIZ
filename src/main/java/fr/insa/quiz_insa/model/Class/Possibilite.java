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

    @NotBlank
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "choix_id")
    private Choix choix;
}