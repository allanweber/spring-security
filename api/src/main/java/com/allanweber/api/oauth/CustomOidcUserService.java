package com.allanweber.api.oauth;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class CustomOidcUserService extends OidcUserService {
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        return new CommonOidUser(super.loadUser(userRequest));
    }
}
