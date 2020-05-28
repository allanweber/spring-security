package com.allanweber.api.auth;

import com.allanweber.api.Api;
import com.allanweber.api.jwt.JwtConstantsHelper;
import com.allanweber.api.jwt.TokenDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Api.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PasswordEncoder encoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    @Test
    public void login_user() throws Exception {
        LoginResponse login = login("user", "123", null);
        assertNotNull(login);
        assertNotNull(login.getToken());
    }

    @Test
    public void login_admin() throws Exception {
        LoginResponse login = login("admin", "123", Collections.singletonList(new Authority("ADMIN")));
        assertNotNull(login);
        assertNotNull(login.getToken());
        assertEquals("ROLE_ADMIN", login.getRoles().get(0));
    }

    @Test
    public void isAdmin_is_false_for_user() throws Exception {
        String token = login("user", "123", null).getToken();
        MockHttpServletResponse response = mockMvc.perform(get("/auth/is-admin")
                .header(JwtConstantsHelper.TOKEN_HEADER, JwtConstantsHelper.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Boolean isAdmin = objectMapper.readValue(response.getContentAsString(), Boolean.class);
        assertFalse(isAdmin);
    }

    @Test
    public void isAdmin_is_true_for_user() throws Exception {
        String token = login("user", "123",  Collections.singletonList(new Authority("ADMIN"))).getToken();
        MockHttpServletResponse response = mockMvc.perform(get("/auth/is-admin")
                .header(JwtConstantsHelper.TOKEN_HEADER, JwtConstantsHelper.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Boolean isAdmin = objectMapper.readValue(response.getContentAsString(), Boolean.class);
        assertTrue(isAdmin);
    }

    @Test
    public void refreshToken() throws Exception {
        String token = login("user", "123", null).getToken();
        MockHttpServletResponse response = mockMvc.perform(get("/auth/refreshToken")
                .header(JwtConstantsHelper.TOKEN_HEADER, JwtConstantsHelper.TOKEN_PREFIX + token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        TokenDto tokenDto = objectMapper.readValue(response.getContentAsString(), TokenDto.class);
        assertNotNull(tokenDto);
        assertNotNull(tokenDto.getToken());
    }

    private LoginResponse login(String user, String password, List<Authority> authorities) throws Exception {
        if (authorities == null) {
            authorities = Collections.singletonList(new Authority("USER"));
        }
        mockUserRepo(user, password, authorities);
        MockHttpServletResponse response = mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new LoginRequest("user", "123"))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        return objectMapper.readValue(response.getContentAsString(), LoginResponse.class);
    }

    private void mockUserRepo(String user, String password, List<Authority> authorities) {
        UserEntity userEntity = new UserEntity(
                user,
                encoder.encode(password),
                "main@gmail.com",
                true,
                authorities,
                true
        );

        when(userRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
    }

}