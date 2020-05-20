package com.allanweber.api.contact;

import com.allanweber.api.contact.mapper.ContactMapper;
import com.allanweber.api.contact.repository.Contact;
import com.allanweber.api.contact.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper mapper = Mappers.getMapper(ContactMapper.class);

    public List<ContactDto> getAll() {
        return contactRepository.findAll()
                .stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }

    public ContactDto getById(String id) {
        return contactRepository.findById(id)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new HttpClientErrorException(NOT_FOUND));
    }

    public ContactDto update(String id, ContactDto contact) {
        return contactRepository.findById(id)
                .map(entity -> mapper.mapToUpdate(id, contact))
                .map(contactRepository::save)
                .map(mapper::mapToDto)
                .orElseThrow(() -> new HttpClientErrorException(NOT_FOUND));
    }

    public ContactDto insert(ContactDto contact) {
        if (contactRepository.existsByNameAndEmail(contact.getName(), contact.getEmail())) {
            throw new HttpClientErrorException(BAD_REQUEST, String.format("Contact %s, email %s already exists.", contact.getName(), contact.getEmail()));
        }
        Contact entity = contactRepository.insert(mapper.mapToEntity(contact));
        return mapper.mapToDto(entity);
    }

    public void delete(String id) {
        contactRepository.deleteById(id);
    }
}
