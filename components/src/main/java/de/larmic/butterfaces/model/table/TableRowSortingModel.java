package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
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
