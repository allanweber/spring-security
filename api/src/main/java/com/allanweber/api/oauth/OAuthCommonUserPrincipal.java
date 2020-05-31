package com.allanweber.api.oauth;

import java.util.List;

public interface OAuthCommonUserPrincipal {
    String getFirstName();
    String getLastName();
    String getUserName();
    String getEmail();
    List<String> getRoles();
}
