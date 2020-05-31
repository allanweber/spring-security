package com.allanweber.api.oauth;

import com.allanweber.api.configuration.AuthoritiesHelper;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collections;
import java.util.List;

public class CommonOidUser extends DefaultOidcUser implements OAuthCommonUserPrincipal {

    private static final long serialVersionUID = -9052922700855524101L;

    public CommonOidUser(OidcUser user) {
        super(user.getAuthorities(), user.getIdToken());
    }

    @Override
    public String getFirstName() {
        return this.getAttributes().get("given_name").toString();
    }

    @Override
    public String getLastName() {
        return this.getAttributes().get("family_name").toString();
    }

    @Override
    public String getUserName() {
        return this.getAttributes().get("email").toString();
    }

    @Override
    public String getEmail() {
        return this.getAttributes().get("email").toString();
    }

    @Override
    public List<String> getRoles() {
        return Collections.singletonList(AuthoritiesHelper.ROLE_USER);
    }

}
