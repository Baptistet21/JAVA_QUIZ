package fr.insa.quiz_insa.model.service;
import fr.insa.quiz_insa.model.DTO.UserDTO;
import fr.insa.quiz_insa.model.Class.Utilisateur;

public interface UserService {

    /* méthode qui permet de creer un user avec un userDTO */
    Utilisateur saveUser(UserDTO userDto);

}
