/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.model.table;

/**
 * @author Lars Michaelis
 */
public interface TableRowSortingModel {

    /**
     * @param tableUniqueIdentifier  table unique identifier (component client if if unique identifier is empty)
     * @param columnUniqueIdentifier column unique identifier (component client if if unique identifier is empty)
     * @param sortBy   optional column component attribute
     * @param sortType the {@link de.larmic.butterfaces.model.table.SortType}
     */
    void sortColumn(final String tableUniqueIdentifier,
                    final String columnUniqueIdentifier,
                    final String sortBy,
                    final SortType sortType);

    SortType getSortType(final String tableUniqueIdentifier, final String columnUniqueIdentifier);
}
