package com.allanweber.api.configuration;

import com.allanweber.api.jwt.JwtUtils;
import com.allanweber.api.registration.UserRegistration;
import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
        String firstName = null;
        String lastName = null;
        String userName;
        String email = null;
        if (isFacebookOrGithub(oauthToken)) {
            String name = attributes.get("name").toString();
            firstName = name.split(" ")[0];
            lastName = name.split(" ")[1];
            email = attributes.get("email").toString();
        } else if (oauthToken.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) oauthToken.getPrincipal();
            firstName = oidcUser.getGivenName();
            lastName = oidcUser.getFamilyName();
            email = oidcUser.getEmail();
        }
        userName = email;
        if (userNotExists(userName, email)) {
            UserRegistration registration = new UserRegistration(firstName, lastName, userName, email);
            userService.createUser(registration);
        }
        var user = userService.loadUserByUsername(userName);
        var roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        var tokenDto = jwtUtils.generateJwtToken(userName, roles);
        redirectStrategy.sendRedirect(request, response, "http://localhost:4200/auth/social-signIn?token="+ tokenDto.getToken());
    }

    private boolean isFacebookOrGithub(OAuth2AuthenticationToken oauthToken) {
        return oauthToken.getAuthorizedClientRegistrationId().equals("facebook") ||
                oauthToken.getAuthorizedClientRegistrationId().equals("github");
    }

    private boolean userNotExists(String userName, String email) {
        return !userService.userNameExists(userName) && !userService.emailExists(email);
    }
}
