package com.allanweber.api.registration;

import com.allanweber.api.Api;
import com.allanweber.api.handler.dto.ResponseErrorDto;
import com.allanweber.api.user.UserDto;
import com.allanweber.api.user.mapper.UserMapper;
import com.allanweber.api.user.repository.UserEntity;
import com.allanweber.api.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Api.class)
class RegistrationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserRepository userRepository;

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    @Test
    public void registerNewUser() throws Exception {
        UserRegistration registration = new UserRegistration("User", "User", "user1", "mail@mail.com",
                "449#5GdPBab6@FQQ", "449#5GdPBab6@FQQ");

        UserEntity entity = mapper.mapToEntity(registration);
        entity.setEnabled(true);
        entity.addAuthority("USER");
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);

        MockHttpServletResponse response = mockMvc.perform(post("/registration/signUp")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        UserDto responseDto = objectMapper.readValue(response.getContentAsString(), UserDto.class);
        assertTrue(responseDto.getEnabled());
        assertEquals(1, responseDto.getAuthorities().size());
        assertEquals("USER", responseDto.getAuthorities().get(0).getAuthority());
    }

    @Test
    public void registerNewUser_userAlreadyExists_shouldFail() throws Exception {
        UserRegistration registration = new UserRegistration("User", "User", "user1", "mail@mail.com",
                "449#5GdPBab6@FQQ", "449#5GdPBab6@FQQ");

        when(userRepository.existsByUserName(registration.getUserName())).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(post("/registration/signUp")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        ResponseErrorDto responseDto = objectMapper.readValue(response.getContentAsString(), ResponseErrorDto.class);
        assertEquals("Username already exists", responseDto.getMessage());
    }

    @Test
    public void registerNewUser_emailAlreadyExists_shouldFail() throws Exception {
        UserRegistration registration = new UserRegistration("User", "User", "user1", "mail@mail.com",
                "449#5GdPBab6@FQQ", "449#5GdPBab6@FQQ");

        when(userRepository.existsByEmail(registration.getEmail())).thenReturn(true);

        MockHttpServletResponse response = mockMvc.perform(post("/registration/signUp")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        ResponseErrorDto responseDto = objectMapper.readValue(response.getContentAsString(), ResponseErrorDto.class);
        assertEquals("Email already exists", responseDto.getMessage());
    }

    @Test
    public void registerNewUser_passwordDoesNotMatch_shouldFail() throws Exception {
        UserRegistration registration = new UserRegistration("User", "User", "user1", "mail@mail.com",
                "449#5GdPBab6@FQQ", "449#5GdPBab6@FQQ1");

        MockHttpServletResponse response = mockMvc.perform(post("/registration/signUp")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        ResponseErrorDto responseDto = objectMapper.readValue(response.getContentAsString(), ResponseErrorDto.class);
        assertEquals("Passwords do not match", responseDto.getMessage());
    }

    @Test
    public void registerNewUser_passwordPolicy_shouldFail() throws Exception {
        UserRegistration registration = new UserRegistration("User", "User", "user1", "mail@mail.com",
                " ", " ");

        MockHttpServletResponse response = mockMvc.perform(post("/registration/signUp")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        ResponseErrorDto responseDto = objectMapper.readValue(response.getContentAsString(), ResponseErrorDto.class);
        assertEquals("Password must be 10 or more characters in length.\n" +
                "Password must contain 1 or more uppercase characters.\n" +
                "Password must contain 1 or more lowercase characters.\n" +
                "Password must contain 1 or more digit characters.\n" +
                "Password must contain 1 or more special characters.\n" +
                "Password matches 0 of 4 character rules, but 3 are required.\n", responseDto.getMessage());
    }
}