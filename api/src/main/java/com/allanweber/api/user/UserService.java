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
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException(userName);
        }

        List<Authority> authorities = authorityRepository.findByUsername(userName);

        if(authorities == null || authorities.size() == 0) {
            throw new UsernameNotFoundException("No authorities for the user");
        }

        String[] roles = authorities.stream().map(Authority::getAuthority).toArray(String[]::new);

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).password(user.getPassword()).roles(roles).build();
    }

    public List<User> getAll(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users.stream().peek(user -> user.setPassword(null)).collect(Collectors.toList());
    }
}
