package com.allanweber.api.user;

import lombok.RequiredArgsConstructor;
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

        List<AuthorityEntity> authorities = authorityRepository.findByUsername(userName);
        if(authorities == null || authorities.isEmpty()) {
            throw new UsernameNotFoundException("No authorities for the user");
        }

        String[] roles = authorities.stream().map(AuthorityEntity::getAuthority).toArray(String[]::new);

        UserEntity user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        return org.springframework.security.core.userdetails.User.
                withUsername(user.getUsername()).password(user.getPassword()).roles(roles).disabled(!user.getVerified()).build();
    }

    public List<UserDto> getAll(){
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
}
