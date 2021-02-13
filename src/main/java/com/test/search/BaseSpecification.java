package com.test.search;

public abstract class BaseSpecification<T, U> {

    private final static String wildcard = "%";

    protected static String startsWith(final String searchField) {
        return searchField.toLowerCase() + wildcard;
    }

    protected static String containsIn(final String searchField) {
        return wildcard + searchField.toLowerCase() + wildcard;
    }

}
