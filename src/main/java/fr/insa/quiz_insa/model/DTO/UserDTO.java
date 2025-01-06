package fr.insa.quiz_insa.model.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    // Définition des propriétés de l'utilisateur avec des annotations de validation

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    @NotBlank(message = "Le prenom ne peut pas être vide")
    private String prenom;

    @NotBlank(message = "Le email ne peut pas être vide")
    @Email(message = "Veuillez saisir une adresse email valide")
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?_-]{8,}$",
            message = "Le mot de passe doit contenir au moins une lettre majuscule, un chiffre, un caractère spécial et 8 caractères minimum")
    private String password;

    private String confirmPassword; // Champ pour confirmer le mot de passe

    private LocalDate date_de_naissance; // Date de naissance de l'utilisateur

    // Champs pour stocker les messages d'erreur éventuels lors de la validation des données
    private String pseudoError;
    private String emailError;
    private String passwordError;
    private String dateError;

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
