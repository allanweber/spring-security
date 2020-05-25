package com.allanweber.api.configuration;

import com.allanweber.api.configuration.filter.TwoFactorAuthenticationFilter;
import com.allanweber.api.two_factor.AuthoritiesHelper;
import com.allanweber.api.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_PATH = new String[]{"/authenticated", "/registration/**"};
    private static final String[] ADMIN_PATH = new String[]{"/admin/contacts/**", "/users/**"};
    private static final String[] AUTH_PATH = new String[]{"/auth/**"};

    private final UserService userService;
    private final TwoFactorAuthenticationFilter twoFactorAuthenticationFilter;
    private final PasswordEncoder encoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterAfter(twoFactorAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(OPTIONS, "/**").permitAll()
                .antMatchers(AUTH_PATH).hasAuthority(AuthoritiesHelper.TWO_AUTH_AUTHORITY)
                .antMatchers(PUBLIC_PATH).permitAll()
                .antMatchers(ADMIN_PATH).hasRole("ADMIN")
                .anyRequest().hasRole("USER")
                .and()
                .httpBasic();

		httpSecurity.cors();
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(encoder);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin(CorsConfiguration.ALL);
        config.addAllowedMethod(CorsConfiguration.ALL);
        config.addAllowedHeader(CorsConfiguration.ALL);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
