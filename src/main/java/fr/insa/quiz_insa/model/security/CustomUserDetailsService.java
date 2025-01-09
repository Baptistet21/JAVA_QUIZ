package fr.insa.quiz_insa.model.security;

import fr.insa.quiz_insa.model.Class.Utilisateur;
import fr.insa.quiz_insa.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository userRepository;

    public CustomUserDetailsService(UtilisateurRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec l'adresse e-mail : " + email);
        }
        return new CustomUserDetails(user);
    }

}

