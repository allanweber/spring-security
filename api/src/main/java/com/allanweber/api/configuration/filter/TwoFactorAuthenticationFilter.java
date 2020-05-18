package com.allanweber.api.configuration.filter;

import com.allanweber.api.two_factor.AuthoritiesHelper;
import com.allanweber.api.two_factor.TwoFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("PMD")
@Component
@RequiredArgsConstructor
public class TwoFactorAuthenticationFilter extends GenericFilterBean {

    private static final String TWO_FACTOR_SETUP = "/auth/setup-two-factor";
    private static final String TWO_CODE = "two_code";

    private final TwoFactorService twoFactorService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String code = obtainCode(request);

        if(!requiresTwoAuthentication(authentication)) {
            chain.doFilter(request, response);
            return;
        }

        if(requiresTwoAuthentication(authentication) && code == null) {
            if(isSetUpTwoFactor((HttpServletRequest) request)){
                chain.doFilter(request, response);
                return;
            }
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else if (requiresTwoAuthentication(authentication) && authentication.isAuthenticated() && codeIsValid(authentication.getName(), code)) {
            Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            authorities.remove(AuthoritiesHelper.TWO_AUTH_AUTHORITY);
            authorities.add(AuthoritiesHelper.ROLE_USER);
            authentication = new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), buildAuthorities(authorities));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            HttpServletResponse res = (HttpServletResponse) response;
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isSetUpTwoFactor(HttpServletRequest request) {
        return Optional.ofNullable(request)
                .map(HttpServletRequest::getRequestURI)
                .map(uri -> uri.equals(TWO_FACTOR_SETUP))
                .orElse(false);
    }

    private boolean requiresTwoAuthentication(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        boolean hasTwoFactorAuth = authorities.contains(AuthoritiesHelper.TWO_AUTH_AUTHORITY);
        return hasTwoFactorAuth && authentication.isAuthenticated();
    }

    private boolean codeIsValid(String username, String code) {
        return code != null && twoFactorService.verifyCode(username, Integer.parseInt(code));
    }

    private String obtainCode(ServletRequest request) {
        return request.getParameter(TWO_CODE);
    }

    private List<GrantedAuthority> buildAuthorities(Collection<String> authorities) {
        List<GrantedAuthority> authList = new ArrayList<>(1);
        for (String authority : authorities) {
            authList.add(new SimpleGrantedAuthority(authority));
        }
        return authList;
    }

}
