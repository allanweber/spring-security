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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));

        List<Authority> authorities = authorityRepository.findByUsername(userName);

        if (authorities == null || authorities.size() == 0) {
            throw new UsernameNotFoundException("No authorities for the user");
        }

        String[] roles = authorities.stream().map(Authority::getAuthority).toArray(String[]::new);

        return org.springframework.security.core.userdetails.User.
                withUsername(user.getUsername()).password(user.getPassword()).roles(roles).disabled(!user.getVerified()).build();
    }

    public List<UserDto> getAll() {
        List<User> users = new ArrayList<>();
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

    public UserDto createUser(User userToCreate) {
        User user = userRepository.save(userToCreate);

        Authority authority = Authority.create(user.getUsername(), "USER");
        authorityRepository.save(authority);

        return new UserDto(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), user.getEnabled());
    }

    public void serUserVerified(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        user.verified();
        userRepository.save(user);
    }
}
