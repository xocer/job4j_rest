package ru.job4j.job4j_rest.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private int inn;
    private LocalDate hiringDate;
    @OneToMany()
    private List<Person> accounts;
}
