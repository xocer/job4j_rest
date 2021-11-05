package ru.job4j.job4j_rest.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.job4j_rest.Job4jRestApplication;
import ru.job4j.job4j_rest.model.Person;
import ru.job4j.job4j_rest.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Job4jRestApplication.class)
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository repository;

    @Autowired
    private Gson gson;

    @Test
    public void whenCreatePersonThenGetCreatedPerson() throws Exception {
        Person person = new Person();
        person.setLogin("test");
        person.setPassword("pwd");
        String jsonOut = gson.toJson(person);

        mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOut))
                .andExpect(status().isCreated());

        ArgumentCaptor<Person> personArg = ArgumentCaptor.forClass(Person.class);
        verify(repository).save(personArg.capture());

        assertEquals("test", personArg.getValue().getLogin());
        assertEquals("pwd", personArg.getValue().getPassword());
    }

    @Test
    public void whenGetAllPersonsThenReturnListOfPersons() throws Exception {
        List<Person> persons = List.of(
                new Person (1,"test1", "pwd1"),
                new Person(2, "test2", "pwd2"),
                new Person(3, "test3", "pwd3")
        );

        String jsonOut = gson.toJson(persons);

        when(repository.findAll()).thenReturn(persons);

        mockMvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonOut));

        verify(repository).findAll();
    }


    @Test
    public void whenFindExistingPersonByIdThenReturnPerson() throws Exception {
        Person person = new Person(1, "login", "pwd");

        when(repository.findById(1)).thenReturn(Optional.of(person));

        String jsonOut = gson.toJson(person);

        mockMvc.perform(get("/person/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonOut));

        verify(repository).findById(anyInt());
    }

    @Test
    public void whenFindMissingPersonByIdThenReturnError() throws Exception {
        String jsonOut = gson.toJson(new Person());

        mockMvc.perform(get("/person/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonOut));

        verify(repository).findById(anyInt());
    }

    @Test
    public void whenDeletePersonThenGetOkStatus() throws Exception {
        mockMvc.perform(delete("/person/1"))
                .andExpect(status().isOk());

        verify(repository).delete(any());
    }

    @Test
    public void whenUpdateExistingPersonThenGetOkStatus() throws Exception {
        String updatedPerson = gson.toJson(
                new Person(1, "upd", "pwd"));

        mockMvc.perform(put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedPerson))
                .andExpect(status().isOk());

        verify(repository).save(any());
    }
}