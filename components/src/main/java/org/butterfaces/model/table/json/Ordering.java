package org.butterfaces.model.table.json;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * DTO and helper class for {@link JsonToModelConverter}.
 */
public class Ordering {
    private final Integer index;
    private final String identifier;

    public Ordering(final String identifier, final int index) {
        this.index = index;
        this.identifier = identifier;
    }

    public Integer getIndex() {
        return index;
    }

    public String getIdentifier() {
        return identifier;
    }

    public static void sort(final List<Ordering> orderings) {
        Collections.sort(orderings, new Comparator<Ordering>() {
            @Override
            public int compare(Ordering o1, Ordering o2) {
                return o1.getIndex().compareTo(o2.getIndex());
            }
        });
    }
}
