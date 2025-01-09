package fr.insa.quiz_insa.model.service;

import fr.insa.quiz_insa.model.DTO.UserDTO;
import fr.insa.quiz_insa.model.Class.Utilisateur;
import fr.insa.quiz_insa.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserServiceImplementation implements UserService{
    private UtilisateurRepository utilisateurRepository;

    private PasswordEncoder passwordEncoder;


    public UserServiceImplementation(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* méthode qui permet de creer un user avec un userDTO */

    @Transactional
    @Override
    public Utilisateur saveUser(UserDTO userDto) {
        Utilisateur existingUser = utilisateurRepository.findByEmail(userDto.getEmail());
        if(existingUser != null) {
            throw new IllegalArgumentException("Un utilisateur avec cet e-mail existe déjà.");
        }

        Utilisateur user = new Utilisateur();
        user.setNom(userDto.getNom());
        user.setPrenom(userDto.getPrenom());
        user.setEmail(userDto.getEmail());
        // encrypt le password
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // donne le role de user
        user.setRole("USER");
        user.setDate_de_naissance(userDto.getDate_de_naissance());
        user.setLastActive(LocalDateTime.now());

        return utilisateurRepository.save(user);
    }



}
