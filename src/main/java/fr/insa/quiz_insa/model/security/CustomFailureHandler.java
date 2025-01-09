package fr.insa.quiz_insa.model.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CustomFailureHandler implements AuthenticationFailureHandler {

    /* message en cas d'echec de connexion */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Adresse e-mail ou mot de passe incorrect.";
        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/connexion?error");
    }
}
