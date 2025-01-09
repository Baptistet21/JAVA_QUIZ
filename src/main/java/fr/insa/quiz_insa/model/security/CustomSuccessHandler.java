package fr.insa.quiz_insa.model.security;
import fr.insa.quiz_insa.model.Class.Utilisateur;
import fr.insa.quiz_insa.repository.UtilisateurRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private UtilisateurRepository utilisateurRepository;

    public CustomSuccessHandler(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /* methode qui permet de donner les redirections */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        var authourities = authentication.getAuthorities();
        var roles = authourities.stream().map(r -> r.getAuthority()).findFirst();

        if (roles.orElse("").equals("ADMIN")) {
            response.sendRedirect("/admin/menu2");
        } else if (roles.orElse("").equals("USER")) {
            // Obtenez l'ID de l'utilisateur à partir de la base de données ou d'où vous le stockez
            String username = authentication.getName();
            Optional<Utilisateur> userOp = Optional.ofNullable(utilisateurRepository.findByEmail(username));
            if (userOp.isPresent()) {
                Utilisateur user = userOp.get();
                Long userId = user.getId();
                user.setLastActive(LocalDateTime.now());
                utilisateurRepository.save(user);
//                response.sendRedirect("/view/user/menu2/" + userId);
                response.sendRedirect("/user/menu2");
            } else {
                response.sendRedirect("/connexion");
            }
        } else {
            response.sendRedirect("/connexion");
        }
    }
}


