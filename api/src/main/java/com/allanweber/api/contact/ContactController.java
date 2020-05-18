package com.allanweber.api.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<Contact>> getAll(Principal principal) {
        return ok(contactService.getAllPersons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> get(@PathVariable("id") int id) {
        return ok(contactService.getPersonById(id));
    }

    @PostMapping
    public ResponseEntity<Contact> save(@RequestBody Contact contact) {
        var response = contactService.save(contact);

        return created(URI.create(String.format("/contacts/%s", response.getId()))).body(response);
    }
}
