package ru.job4j.job4j_rest.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.job4j_rest.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
}
