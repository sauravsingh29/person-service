package com.test.search;

import com.test.entity.Person;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class PersonSearchSpecification extends BaseSpecification<Person, String> {

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    public Specification<Person> getNameFilter(String firstName, String lastName) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                where(name(firstName, FIRST_NAME).or(contains(firstName, FIRST_NAME).or(name(lastName, LAST_NAME)))
                        .or(contains(lastName, LAST_NAME)))
                        .toPredicate(root, criteriaQuery,
                                criteriaBuilder));
    }


    public Specification<Person> name(String request, String attribute) {
        return (root, query, cb) -> {
            if (request == null) {
                return null;
            }
            return cb.like(cb.lower(root.get(attribute)), startsWith(request));
        };

    }

    public Specification<Person> contains(String request, String attribute) {
        return (root, query, cb) -> {
            if (request == null) {
                return null;
            }
            return cb.like(cb.lower(root.get(attribute)), containsIn(request));
        };

    }
}
