package fr.insa.quiz_insa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Questionnaire extends Question implements Serializable {

    @NotBlank(message = "Le thème ne peut pas être vide")
    private String theme;

    private int niveau;

    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "questionnaire_note", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Note> notes;

    @OneToMany(mappedBy = "questionnaire", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Question> questions;

    @Transient
    private String niveauError;
}