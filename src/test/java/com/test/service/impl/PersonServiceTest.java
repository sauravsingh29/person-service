package com.test.service.impl;

import com.test.entity.Person;
import com.test.exception.PersonServiceException;
import com.test.repository.PersonRepository;
import com.test.request.PersonRequest;
import com.test.search.PersonSearchSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonSearchSpecification personSearchSpecification;

    private PersonService personService;

    private PersonRequest personRequest = buildPersonRequest();

    @BeforeEach
    public void setup() {
        personService = new PersonService(personRepository, personSearchSpecification);
    }

    @Test
    @Order(1)
    void save() {
        Person person = new Person();
        BeanUtils.copyProperties(personRequest, person);
        person.setId(1l);
        when(personRepository.save(any(Person.class))).thenReturn(person);
        Person save = personService.save(personRequest);
        assertNotNull(save);
        assertEquals(1l, save.getId());
    }

    @Test()
    @Order(2)
    void save_failed() {
        when(personRepository.save(any(Person.class))).thenThrow(new RuntimeException("Duplicate id detected."));
        assertThrows(PersonServiceException.class, () -> personService.save(buildPersonRequest()));
    }

    @Test
    @Order(3)
    void findById() {
        Person person = getPerson();
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        Person byId = personService.findById(1l);
        assertNotNull(byId);
        assertEquals(1l, byId.getId());
    }

    @Test
    @Order(4)
    void findById_notfound() {
        Person person = getPerson();
        when(personRepository.findById(anyLong())).thenThrow(new PersonServiceException("No person found with id " + person.getId(),
                NOT_FOUND, "findById"));
        assertThrows(PersonServiceException.class, () -> personService.findById(1l));
    }

    @Test
    @Order(5)
    void findAll() {
        Page<Person> personPage = new PageImpl<>(Arrays.asList(getPerson()));
        when(personRepository.findAll(any(PageRequest.class))).thenReturn(personPage);
        List<Person> persons = personService.findAll(PageRequest.of(0, 10, Sort.by("id")));
        assertNotNull(persons);
        assertEquals(1, persons.size());
    }

    @Test
    @Order(6)
    void findAll_failed() {
        when(personRepository.findAll(any(PageRequest.class))).thenThrow(new RuntimeException("Unable to connect to database"));
        assertThrows(PersonServiceException.class, () -> personService.findAll(PageRequest.of(0, 10, Sort.by("id"))));
    }

    @Test
    @Order(7)
    void findAllByName() {
        when(personSearchSpecification.getNameFilter(anyString(), anyString())).thenReturn((Specification<Person>) (root, cq, cb) -> null);
        when(personRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(getPerson()));
        List<Person> personList = personService.findAllByName("Saurav", "Singh");
        assertEquals(1, personList.size());
    }

    @Test
    @Order(8)
    void findAllByName_notfound() {
        when(personSearchSpecification.getNameFilter(anyString(), anyString())).thenReturn((Specification<Person>) (root, cq, cb) -> null);
        when(personRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        List<Person> personList = personService.findAllByName("Saurav", "");
        assertEquals(0, personList.size());
    }

    @Test
    @Order(9)
    void findAllByName_error() {
        when(personSearchSpecification.getNameFilter(anyString(), anyString())).thenReturn((Specification<Person>) (root, cq, cb) -> null);
        when(personRepository.findAll(any(Specification.class))).thenThrow(new RuntimeException("Unknown error"));
        assertThrows(PersonServiceException.class, () -> personService.findAllByName("Saurav", "Singh"));
    }

    @Test
    @Order(10)
    void update() {
        Person person = getPerson();
        personRequest.setFirstName("John");
        personRequest.setLastName("Keynes");
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(getPerson());
        personService.update(1l, personRequest);
        verify(personRepository).findById(anyLong());
        verify(personRepository).save(any(Person.class));
    }

    @Test
    @Order(11)
    void delete() {
        doNothing().when(personRepository).deleteById(anyLong());
        personService.delete(1l);
        verify(personRepository).deleteById(anyLong());
    }

    private PersonRequest buildPersonRequest() {
        PersonRequest personRequest = new PersonRequest();
        personRequest.setAge(30);
        personRequest.setFirstName("Saurav");
        personRequest.setLastName("Singh");
        personRequest.setFavouriteColour("Black");
        return personRequest;
    }

    private Person getPerson() {
        Person person = new Person();
        BeanUtils.copyProperties(personRequest, person);
        person.setId(1l);
        return person;
    }
}