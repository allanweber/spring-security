package com.allanweber.api.contact.mapper;

import com.allanweber.api.contact.ContactDto;
import com.allanweber.api.contact.repository.Contact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
class ContactMapperTest {

    private final ContactMapper mapper = Mappers.getMapper(ContactMapper.class);

    @Test
    public void mapToDto(){
        Contact entity = new Contact("id", "name", 1, "email", "phone");
        ContactDto dto = mapper.mapToDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getAge(), dto.getAge());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getPhone(), dto.getPhone());
    }

    @Test
    public void mapToEntity(){
        ContactDto dto = new ContactDto("id", "name", 1, "email", "phone");
        Contact entity = mapper.mapToEntity(dto);

        assertNotNull(dto);
        assertNull(entity.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getAge(), dto.getAge());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getPhone(), dto.getPhone());
    }

    @Test
    public void mapToUpdate(){
        ContactDto dto = new ContactDto("id", "name", 1, "email", "phone");
        Contact entity = mapper.mapToUpdate("id", dto);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getAge(), dto.getAge());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getPhone(), dto.getPhone());
    }
}