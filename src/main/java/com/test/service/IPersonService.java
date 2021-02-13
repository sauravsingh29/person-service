package com.test.service;

import com.test.entity.Person;
import com.test.exception.PersonServiceException;
import com.test.request.PersonRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;

import java.util.List;

public interface IPersonService {

    /**
     * <p>
     * Storing person entity to database.
     * </p>
     *
     * @param personRequest {@link PersonRequest} request to be stored in database.
     * @return Person object
     * @throws PersonServiceException
     */
    Person save(final @NonNull PersonRequest personRequest) throws PersonServiceException;

    /**
     * <p>
     * Find a person with given id.
     * </p>
     *
     * @param id {@link Long} Person's id.
     * @return Person object if found any else null.
     * @throws PersonServiceException
     */
    Person findById(final @NonNull Long id) throws PersonServiceException;

    /**
     * <p>
     * Find all person stored in memory DB.
     * </p>
     *
     * @param pageRequest Pageable request object {@link PageRequest}
     * @return List of Person object if found any else empty list.
     * @throws PersonServiceException
     */
    List<Person> findAll(final @NonNull PageRequest pageRequest) throws PersonServiceException;

    /**
     * <p>
     * Find all person who's first name matches like pattern & stored in memory DB.
     * </p>
     *
     * @param firstName FirstName of person
     * @param lastName  LastName of person
     * @return List of Person object if found any else empty list.
     * @throws PersonServiceException
     */
    List<Person> findAllByName(final String firstName, final String lastName) throws PersonServiceException;

    /**
     * <p>
     * Delete a person by person id.
     * </p>
     *
     * @param personId Person's id.
     * @throws PersonServiceException
     */
    void delete(final @NonNull Long personId) throws PersonServiceException;

    /**
     * <p>
     * Update person details.
     * </>
     *
     * @param id
     * @param person
     */
    void update(final @NonNull Long id, final @NonNull PersonRequest person) throws PersonServiceException;
}
