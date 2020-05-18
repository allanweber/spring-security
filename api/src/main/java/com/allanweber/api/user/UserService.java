package com.allanweber.api.user;

import com.allanweber.api.two_factor.AuthoritiesHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {

        List<AuthorityEntity> authorityEntities = authorityRepository.findByUsername(userName);
        if (authorityEntities == null || authorityEntities.isEmpty()) {
            throw new UsernameNotFoundException("No authorities for the user");
        }

        UserEntity user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(userName));

        List<String> authorities = new ArrayList<>();
        if (user.getTwoFactor()) {
            authorities.add(AuthoritiesHelper.TWO_AUTH_AUTHORITY);
        } else {
            authorities.add(AuthoritiesHelper.ROLE_USER);
            if(authorityEntities.stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))){
                authorities.add("ROLE_ADMIN");
            }
        }
        return org.springframework.security.core.userdetails.User.
                withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.getVerified())
                .authorities(buildAuthorities(authorities))
                .build();
    }

    public List<UserDto> getAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users.stream().map(user ->
                new UserDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getEnabled()))
                .collect(Collectors.toList());
    }

    public Boolean userNameExists(String userName) {
        return userRepository.existsByUsername(userName);
    }

    public Boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserDto createUser(UserEntity userToCreate) {
        UserEntity user = userRepository.save(userToCreate);

        AuthorityEntity authority = AuthorityEntity.create(user.getUsername(), "USER");
        authorityRepository.save(authority);

        return new UserDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getEnabled());
    }

    public void setUserVerified(String userName) {
        UserEntity user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        user.markVerified();
        userRepository.save(user);
    }

    public void enableTwoFactorAuthentication(Integer userId) {
        changeTwoFactorAuthentication(userId, true);
    }

    public void disableTwoFactorAuthentication(Integer userId) {
        changeTwoFactorAuthentication(userId, false);
    }

    private void changeTwoFactorAuthentication(Integer userId, boolean value) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId.toString()));
        user.setTwoFactor(value);
        userRepository.save(user);
    }

    private List<GrantedAuthority> buildAuthorities(List<String> authorities) {
        List<GrantedAuthority> authList = new ArrayList<>(1);
        for (String authority : authorities) {
            authList.add(new SimpleGrantedAuthority(authority));
        }
        return authList;
    }
}
