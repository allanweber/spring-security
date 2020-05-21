package com.allanweber.api.contact.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, String> {

    Boolean existsByNameAndEmail(String name, String email);
}
