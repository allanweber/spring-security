package com.allanweber.api.contact;

import com.allanweber.api.Api;
import com.allanweber.api.contact.mapper.ContactMapper;
import com.allanweber.api.contact.repository.Contact;
import com.allanweber.api.contact.repository.ContactRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Api.class)
@AutoConfigureMockMvc
class ContactManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContactRepository repository;

    private final ContactMapper mapper = Mappers.getMapper(ContactMapper.class);

    @Test
    public void deleteContact() throws Exception {
        String id = "123";
        doNothing().when(repository).deleteById(id);

        mockMvc.perform(delete("/admin/contacts/{id}", id))
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
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        ContactDto responseDto = objectMapper.readValue(response.getContentAsString(), ContactDto.class);
        assertNotNull(responseDto.getId());
    }
}