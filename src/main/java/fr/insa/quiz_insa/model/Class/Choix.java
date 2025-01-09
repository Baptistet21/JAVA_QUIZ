package fr.insa.quiz_insa.model;

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
public class Choix implements Serializable {
    @Id @GeneratedValue
    private long id;

    @OneToMany(mappedBy = "choix", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Possibilite> possibilites;
}