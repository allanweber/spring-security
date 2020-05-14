package com.allanweber.api.contact;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactRepository extends CrudRepository<Contact, Integer> {

    Boolean existsByNameAndEmail(String name, String email);
}
