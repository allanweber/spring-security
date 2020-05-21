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
import java.util.List;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public ResponseEntity<List<ContactDto>> getAll() {
        return ok(contactService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> get(@PathVariable("id") String id) {
        return ok(contactService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ContactDto> save(@RequestBody ContactDto contact) {
        var response = contactService.insert(contact);
        return created(URI.create(String.format("/contacts/%s", response.getId()))).body(response);
    }
}
