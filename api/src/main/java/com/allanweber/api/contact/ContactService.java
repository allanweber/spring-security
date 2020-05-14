package com.allanweber.api.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;

    public List<Contact> getAllPersons() {
        List<Contact> contacts = new ArrayList<>();
        contactRepository.findAll().forEach(contacts::add);
        return contacts;
    }

    public Contact getPersonById(int id) {
        return contactRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(NOT_FOUND));
    }

    public Contact update(int id, Contact contact) {
        Contact entity = contactRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(NOT_FOUND));
        contact.setId(entity.getId());
        return contactRepository.save(contact);
    }

    public Contact save(Contact contact) {
        if (contactRepository.existsByNameAndEmail(contact.getName(), contact.getEmail())) {
            throw new HttpClientErrorException(BAD_REQUEST, String.format("Contact %s, email %s already exists.", contact.getName(), contact.getEmail()));
        }
        return contactRepository.save(contact);
    }

    public void delete(int id) {
        contactRepository.deleteById(id);
    }
}
