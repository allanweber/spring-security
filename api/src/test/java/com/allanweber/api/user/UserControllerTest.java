package com.allanweber.api.user;

import com.allanweber.api.Api;
import com.allanweber.api.auth.LoginRequest;
import com.allanweber.api.auth.LoginResponse;
import com.allanweber.api.jwt.JwtConstantsHelper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Api.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PasswordEncoder encoder;

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
        String token = login("ADMIN").getToken();
        when(userRepository.findAll()).thenReturn(Arrays.asList(getUser("USER"), getUser("ADMIN")));
        MockHttpServletResponse response = mockMvc.perform(get("/users")
                .header(JwtConstantsHelper.TOKEN_HEADER, JwtConstantsHelper.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        List<UserDto> users = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(2, users.size());
    }

    @Test
    public void getAllUsers_shouldFail_when_UserIsNotAdmin() throws Exception {
        String token = login("USER").getToken();
        MockHttpServletResponse response = mockMvc.perform(get("/users")
                .header(JwtConstantsHelper.TOKEN_HEADER, JwtConstantsHelper.TOKEN_PREFIX + token))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    private LoginResponse login(String role) throws Exception {
        mockUserRepo(role);
        MockHttpServletResponse response = mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new LoginRequest("user", "123"))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        return objectMapper.readValue(response.getContentAsString(), LoginResponse.class);
    }

    private void mockUserRepo(String role){
        when(userRepository.findById(anyString())).thenReturn(Optional.of(getUser(role)));
    }

    private UserEntity getUser(String role){
        List<Authority> authorities = Collections.singletonList(new Authority(role));
        return new UserEntity(
                "user",
                encoder.encode("123"),
                "main@gmail.com",
                true,
                authorities,
                true
        );
    }
}