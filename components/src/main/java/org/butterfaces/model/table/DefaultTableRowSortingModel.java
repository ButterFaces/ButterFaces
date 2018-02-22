/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.model.table;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lars Michaelis
 */
public class DefaultTableRowSortingModel implements TableRowSortingModel {

    /**
     * Map of column id and {@link org.butterfaces.model.table.SortType} information.
     */
    protected final Map<String, SortType> columnSortTypes = new HashMap<>();

    /**
     * Resets all sort orders to NONE and sets {@link org.butterfaces.model.table.SortType} for corresponding column id.
     *
     * @param columnUniqueIdentifier optional column id (generated id will be used is column id attribute is not set)
     * @param sortBy                 optional column component attribute
     * @param sortType               the {@link org.butterfaces.model.table.SortType}
     */
    @Override
    public void sortColumn(final String tableUniqueIdentifier,
                           final String columnUniqueIdentifier,
                           final String sortBy,
                           final SortType sortType) {
        for (String key : columnSortTypes.keySet()) {
            columnSortTypes.put(key, SortType.NONE);
        }

        columnSortTypes.put(columnUniqueIdentifier, sortType);
    }

    @Override
    public SortType getSortType(final String tableUniqueIdentifier, final String columnUniqueIdentifier) {
        return columnSortTypes.get(columnUniqueIdentifier);
    }
}
