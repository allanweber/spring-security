package com.allanweber.api.user;

import com.allanweber.api.user.mapper.UserMapper;
import com.allanweber.api.user.repository.UserDetailsHelper;
import com.allanweber.api.user.repository.UserEntity;
import com.allanweber.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Override
    public UserDetails loadUserByUsername(String userName) {
        UserEntity user = userRepository.findById(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        if(user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            throw new UsernameNotFoundException("No authorities for the user");
        }
        return UserDetailsHelper.createUserDetails(user);
    }

    public List<UserDto> getAll(){
        return userRepository.findAll().stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }
}
