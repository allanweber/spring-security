package com.allanweber.api.user;

import com.allanweber.api.Api;
import com.allanweber.api.user.repository.Authority;
import com.allanweber.api.user.repository.UserEntity;
import com.allanweber.api.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Api.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    @Test
    public void getAllUsers() throws Exception {
        mockUserRepo("ADMIN");
        when(userRepository.findAll()).thenReturn(Arrays.asList(getUser("USER"), getUser("ADMIN")));
        MockHttpServletResponse response = mockMvc.perform(get("/users")
                .with(httpBasic("user", "123")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        List<UserDto> users = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(2, users.size());
    }

    @Test
    public void getAllUsers_shouldFail_when_UserIsNotAdmin() throws Exception {
        mockUserRepo("USER");
        MockHttpServletResponse response = mockMvc.perform(get("/users")
                .with(httpBasic("user", "123")))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    @Test
    public void enableTwoFactorAuthentication() throws Exception {
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        mockUserRepo("ADMIN");
        MockHttpServletResponse response = mockMvc.perform(put("/users/{userName}/enable-two-factor", "user")
                .with(httpBasic("user", "123")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
    }

    @Test
    public void enableTwoFactorAuthentication_shouldFail_when_UserIsNotAdmin() throws Exception {
        mockUserRepo("USER");
        MockHttpServletResponse response = mockMvc.perform(put("/users/{userName}/enable-two-factor", "user")
                .with(httpBasic("user", "123")))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    @Test
    public void disableTwoFactorAuthentication() throws Exception {
        when(userRepository.save(any(UserEntity.class))).thenReturn(null);
        mockUserRepo("ADMIN");
        MockHttpServletResponse response = mockMvc.perform(put("/users/{userName}/disable-two-factor", "user")
                .with(httpBasic("user", "123")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
    }

    @Test
    public void disableTwoFactorAuthentication_shouldFail_when_UserIsNotAdmin() throws Exception {
        mockUserRepo("USER");
        MockHttpServletResponse response = mockMvc.perform(put("/users/{userName}/disable-two-factor", "user")
                .with(httpBasic("user", "123")))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    private void mockUserRepo(String role){
        when(userRepository.findById(anyString())).thenReturn(Optional.of(getUser(role)));
    }

    private UserEntity getUser(String role){
        List<Authority> authorities = Collections.singletonList(new Authority(role));
        return new UserEntity(
                "user",
                "$2a$10$6gT7XuiWtHR1hXHQuDb54.rG3TgUNrrpTff8WE15sf4dkmYMKyd1y",
                "main@gmail.com",
                true,
                authorities,
                true,
                false
        );
    }
}