package com.allanweber.api.contact;

import com.allanweber.api.Api;
import com.allanweber.api.contact.mapper.ContactMapper;
import com.allanweber.api.contact.repository.Contact;
import com.allanweber.api.contact.repository.ContactRepository;
import com.allanweber.api.user.repository.Authority;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Api.class)
class ContactManagementControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ContactRepository repository;

    @MockBean
    private UserRepository userRepository;

    private final ContactMapper mapper = Mappers.getMapper(ContactMapper.class);

    @BeforeEach
    public void setUp() {
        mockUserRepo();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    @Test
    public void deleteContact() throws Exception {
        String id = "123";
        doNothing().when(repository).deleteById(id);

        mockMvc.perform(delete("/admin/contacts/{id}", id)
                .with(httpBasic("user","123")))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void putContact() throws Exception {
        String id = "123";
        ContactDto dto = new ContactDto(id, "name", 1, "email", "phone");
        Contact entity = mapper.mapToEntity(dto);
        entity.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(any(Contact.class))).thenReturn(entity);

        MockHttpServletResponse response = mockMvc.perform(put("/admin/contacts/{id}", id)
                .with(httpBasic("user","123"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        ContactDto responseDto = objectMapper.readValue(response.getContentAsString(), ContactDto.class);
        assertNotNull(responseDto.getId());
    }

    @Test
    public void deleteContact_anonymous_should_fail() throws Exception {
        String id = "123";
        mockMvc.perform(delete("/admin/contacts/{id}", id))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void putContact_anonymous_should_fail() throws Exception {
        String id = "123";
        ContactDto dto = new ContactDto(id, "name", 1, "email", "phone");

        mockMvc.perform(put("/admin/contacts/{id}", id)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    private void mockUserRepo(){
        when(userRepository.findById(anyString())).thenReturn(Optional.of(getUser()));
    }

    private UserEntity getUser(){
        List<Authority> authorities = Collections.singletonList(new Authority("ADMIN"));
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