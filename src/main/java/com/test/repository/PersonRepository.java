package com.test.repository;

import com.test.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends PagingAndSortingRepository<Person, Long>, JpaSpecificationExecutor<Person> {

    List<Person> findByFirstNameIgnoreCase(final String firstName);

    Page<Person> findByFirstNameIsLike(final String firstName, Pageable pageable);

}
