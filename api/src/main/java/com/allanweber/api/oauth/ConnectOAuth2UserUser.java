package com.allanweber.api.oauth;

import com.allanweber.api.configuration.AuthoritiesHelper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class ConnectOAuth2UserUser implements OAuth2User, OAuthCommonUserPrincipal {
    private String name;
    private String id;
    private String email;
    private List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(AuthoritiesHelper.ROLE_USER);
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
            this.attributes.put("id", this.getId());
            this.attributes.put("name", this.getName());
            this.attributes.put("email", this.getEmail());
            this.attributes.put("given_name", this.getName().split(" ")[0]);
            this.attributes.put("family_name", this.getName().split(" ")[1]);
        }
        return attributes;
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
        return this.email;
    }

    @Override
    public List<String> getRoles() {
        return this.authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
