package com.allanweber.api.contact;

import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Integer> {

    Boolean existsByNameAndEmail(String name, String email);
}
