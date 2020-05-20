package com.allanweber.api.contact.mapper;

import com.allanweber.api.contact.ContactDto;
import com.allanweber.api.contact.repository.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ContactMapper {

    @Mapping(target = "id", ignore = true)
    Contact mapToEntity(ContactDto dto);

    @Mapping(target = "id", source ="id")
    Contact mapToUpdate(String id, ContactDto dto);

    ContactDto mapToDto(Contact entity);
}
