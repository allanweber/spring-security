package com.allanweber.api.configuration;

import com.allanweber.api.jwt.JwtUtils;
import com.allanweber.api.oauth.OAuthCommonUserPrincipal;
import com.allanweber.api.registration.UserRegistration;
import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final ApplicationConfiguration appConfiguration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuthCommonUserPrincipal principal = (OAuthCommonUserPrincipal)authentication.getPrincipal();
        if (userNotExists(principal)) {
            UserRegistration registration = new UserRegistration(principal.getFirstName(), principal.getLastName(), principal.getUserName(), principal.getEmail());
            userService.createUser(registration);
        }
        var tokenDto = jwtUtils.generateJwtToken(principal.getUserName(), principal.getRoles());

        String url = appConfiguration.getAppUrl() + "/auth/social-signIn?token="+ tokenDto.getToken();
        redirectStrategy.sendRedirect(request, response, url);
    }

    private boolean userNotExists(OAuthCommonUserPrincipal principal) {
        return !userService.userNameExists(principal.getUserName()) && !userService.emailExists(principal.getEmail());
    }
}
