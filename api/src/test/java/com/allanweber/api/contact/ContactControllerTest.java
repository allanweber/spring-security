package com.allanweber.api.contact;

import com.allanweber.api.Api;
import com.allanweber.api.contact.mapper.ContactMapper;
import com.allanweber.api.contact.repository.Contact;
import com.allanweber.api.contact.repository.ContactRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Api.class)
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContactRepository repository;

    private final ContactMapper mapper = Mappers.getMapper(ContactMapper.class);

    @Test
    public void postContact() throws Exception {
        ContactDto dto = new ContactDto(null, "name", 1, "email", "phone");
        Contact entity = mapper.mapToEntity(dto);
        entity.setId("123");

        when(repository.insert(any(Contact.class))).thenReturn(entity);

        MockHttpServletResponse response = mockMvc.perform(post("/contacts")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        ContactDto responseDto = objectMapper.readValue(response.getContentAsString(), ContactDto.class);
        assertNotNull(responseDto.getId());
    }

    @Test
    public void getContactById() throws Exception {
        String id = "123";
        Contact contact = new Contact(id, "name", 1, "email", "phone");
        when(repository.findById(id)).thenReturn(Optional.of(contact));

        MockHttpServletResponse response = mockMvc.perform(get("/contacts/{id}", id))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        ContactDto responseDto = objectMapper.readValue(response.getContentAsString(), ContactDto.class);
        assertNotNull(responseDto.getId());
    }

    @Test
    public void getAll() throws Exception {
        List<Contact> contacts = Arrays.asList(
                new Contact("123", "name", 1, "email", "phone"),
                new Contact("456", "name", 1, "email", "phone")
        );

        when(repository.findAll()).thenReturn(contacts);

        MockHttpServletResponse response = mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<ContactDto> dtos = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<ContactDto>>() {});

        assertEquals(2, dtos.size());
        assertEquals("123", dtos.get(0).getId());
        assertEquals("456", dtos.get(1).getId());
    }
}