package com.saucedemo.pages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Name sort options of the product dropdown.
 *
 * Polymorphism: each constant overrides {@link #comparator()}, so a test can ask any
 * SortOrder for the expected order without an if/else on which option was picked.
 */
public enum SortOrder {

    NAME_A_TO_Z("az") {
        @Override
        public Comparator<String> comparator() {
            return Comparator.naturalOrder();
        }
    },
    NAME_Z_TO_A("za") {
        @Override
        public Comparator<String> comparator() {
            return Comparator.reverseOrder();
        }
    };

    private final String optionValue;

    SortOrder(String optionValue) {
        this.optionValue = optionValue;
    }

    public abstract Comparator<String> comparator();

    /** Value of the matching option in the sort dropdown. */
    public String optionValue() {
        return optionValue;
    }

    /** Returns a sorted copy of the given names. */
    public List<String> sort(List<String> names) {
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(comparator());
        return sorted;
    }
}
