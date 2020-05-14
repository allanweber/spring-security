package com.allanweber.api.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping("/admin/contacts")
@RequiredArgsConstructor
public class ContactManagementController {

    private final ContactService contactService;

    @DeleteMapping("/{id}")
    private ResponseEntity<?> delete(@PathVariable("id") int id) {
        contactService.delete(id);
        return noContent().build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<Contact> save(@PathVariable("id") int id, @RequestBody Contact contact) {
        return ResponseEntity.ok(contactService.update(id, contact));
    }
}
