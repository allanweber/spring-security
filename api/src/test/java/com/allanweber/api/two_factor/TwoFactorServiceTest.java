package com.allanweber.api.two_factor;

import com.allanweber.api.Api;
import com.allanweber.api.two_factor.repository.TwoFactor;
import com.allanweber.api.two_factor.repository.TwoFactorRepository;
import com.allanweber.api.user.repository.Authority;
import com.allanweber.api.user.repository.UserEntity;
import com.allanweber.api.user.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Api.class)
class TwoFactorServiceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TwoFactorRepository twoFactorRepository;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    @Test
    public void setupTwoFactor() throws Exception {

        mockUserRepo(true);

        doNothing().when(twoFactorRepository).deleteById(anyString());
        when(twoFactorRepository.save(any(TwoFactor.class))).thenReturn(null);

        MockHttpServletResponse response = mockMvc.perform(get("/auth/setup-two-factor")
                .with(httpBasic("user", "123")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertTrue(response.getContentAsString().contains("https://chart.googleapis.com"));
    }

    @Test
    public void setupTwoFactor_fails_when_not_enabled() throws Exception {
        mockUserRepo(false);
        MockHttpServletResponse response = mockMvc.perform(get("/auth/setup-two-factor")
                .with(httpBasic("user", "123")))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();
    }

    private void mockUserRepo(boolean twoFactorEnabled){
        when(userRepository.findById(anyString())).thenReturn(Optional.of(getUser(twoFactorEnabled)));
    }

    private UserEntity getUser(boolean twoFactorEnabled){
        List<Authority> authorities = Collections.singletonList(new Authority("USER"));
        return new UserEntity(
                "user",
                "$2a$10$6gT7XuiWtHR1hXHQuDb54.rG3TgUNrrpTff8WE15sf4dkmYMKyd1y",
                "main@gmail.com",
                true,
                authorities,
                true,
                twoFactorEnabled
        );
    }
}