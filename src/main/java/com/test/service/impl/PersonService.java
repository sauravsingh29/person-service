package com.test.service.impl;

import com.test.entity.Person;
import com.test.exception.PersonServiceException;
import com.test.repository.PersonRepository;
import com.test.request.PersonRequest;
import com.test.search.PersonSearchSpecification;
import com.test.service.IPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
public class PersonService implements IPersonService {

    private final PersonRepository personRepository;
    private final PersonSearchSpecification personSearchSpecification;

    @Autowired
    public PersonService(PersonRepository personRepository, PersonSearchSpecification personSearchSpecification) {
        this.personRepository = personRepository;
        this.personSearchSpecification = personSearchSpecification;
    }

    @Override
    public Person save(final @NonNull PersonRequest personRequest) throws PersonServiceException {
        try {
            final Person person = new Person();
            BeanUtils.copyProperties(personRequest, person);
            return personRepository.save(person);
        } catch (Exception e) {
            log.error("Failed to save person {}. Exception:: ", personRequest, e);
            throw new PersonServiceException("Failed to save person.", e, INTERNAL_SERVER_ERROR, "save");
        }
    }

    @Override
    public Person findById(Long id) throws PersonServiceException {
        try {
            return personRepository.findById(id).orElseThrow(() -> new PersonServiceException("No person found with id " + id,
                    NOT_FOUND, "findById"));
        } catch (Exception e) {
            if (e instanceof PersonServiceException) {
                throw e;
            }
            log.error("Failed to find person for id {}. Exception:: ", id, e);
            throw new PersonServiceException("Failed to find person.", e, INTERNAL_SERVER_ERROR, "findById");
        }
    }

    @Override
    public List<Person> findAll(final PageRequest pageRequest) throws PersonServiceException {
        try {
            return personRepository.findAll(pageRequest).getContent();
        } catch (Exception e) {
            log.error("Failed to find all person for page request {}. Exception:: ", pageRequest, e);
            throw new PersonServiceException("Failed to find all person for page request.", e, INTERNAL_SERVER_ERROR, "findAll");
        }
    }

    @Override
    public List<Person> findAllByName(final String firstName, final String lastName) throws PersonServiceException {
        try {
            return personRepository.findAll(personSearchSpecification.getNameFilter(firstName, lastName));
        } catch (Exception e) {
            log.error("Failed to search person where first name {} and lastName {}. Exception:: ", firstName, lastName, e);
            throw new PersonServiceException("Failed to search person.", e,
                    INTERNAL_SERVER_ERROR, "findAllByName");
        }
    }

    @Override
    public void delete(Long personId) throws PersonServiceException {
        try {
            personRepository.deleteById(personId);
        } catch (Exception e) {
            if (e instanceof EmptyResultDataAccessException) {
                log.error("No person found with id {}. Exception:: ", personId, e);
                throw new PersonServiceException("No person found with id " + personId, e, NOT_FOUND, "delete");
            }
            log.error("Failed to delete person for id {}. Exception:: ", personId, e);
            throw new PersonServiceException("Failed to delete person.", e, INTERNAL_SERVER_ERROR, "delete");
        }
    }

    @Override
    public void update(Long id, PersonRequest personRequest) throws PersonServiceException {
        try {
            Person person = personRepository.findById(id).orElseThrow(() ->
                    new PersonServiceException("No person found with id " + id, NOT_FOUND, "update"));
            BeanUtils.copyProperties(personRequest, person);
            personRepository.save(person);
        } catch (Exception e) {
            if (e instanceof PersonServiceException) {
                throw e;
            }
            log.error("Failed to update person for id {}. Exception:: ", id, e);
            throw new PersonServiceException("Failed to update person id " + id, e, INTERNAL_SERVER_ERROR, "delete");
        }
    }
}
