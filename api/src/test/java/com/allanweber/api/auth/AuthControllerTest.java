package com.allanweber.api.auth;

import com.allanweber.api.Api;
import com.allanweber.api.user.UserDto;
import com.allanweber.api.user.repository.Authority;
import com.allanweber.api.user.repository.UserEntity;
import com.allanweber.api.user.repository.UserRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Api.class)
class AuthControllerTest {

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
    public void login_USER() throws Exception {
        mockUserRepo();
        MockHttpServletResponse response = mockMvc.perform(get("/login")
                .with(httpBasic("user", "123")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        UserDto user = objectMapper.readValue(response.getContentAsString(), UserDto.class);
        assertNotNull(user);
    }

    @Test
    public void authenticated_USER() throws Exception {
        mockUserRepo();
        MockHttpServletResponse response = mockMvc.perform(get("/authenticated")
                .with(httpBasic("user", "123")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Boolean authenticated = objectMapper.readValue(response.getContentAsString(), Boolean.class);
        assertTrue(authenticated);
    }

    @Test
    public void login_anonymous() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void authenticated_anonymous() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/authenticated"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Boolean authenticated = objectMapper.readValue(response.getContentAsString(), Boolean.class);
        assertFalse(authenticated);
    }

    private void mockUserRepo() {
        when(userRepository.findById(anyString())).thenReturn(Optional.of(getUser()));
    }

    private UserEntity getUser() {
        List<Authority> authorities = Collections.singletonList(new Authority("USER"));
        return new UserEntity(
                "user",
                "$2a$10$6gT7XuiWtHR1hXHQuDb54.rG3TgUNrrpTff8WE15sf4dkmYMKyd1y",
                "main@gmail.com",
                true,
                authorities,
                true
        );
    }
}