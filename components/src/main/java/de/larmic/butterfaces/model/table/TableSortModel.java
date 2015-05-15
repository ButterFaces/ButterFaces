package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public interface TableSortModel {

    /**
     * @param columnUniqueIdentifier column unique identifier (client id if unique identifier is not set)
     * @param sortBy   optional column component attribute
     * @param sortType the {@link de.larmic.butterfaces.model.table.SortType}
     */
    void sortColumn(final String tableUniqueIdentifier,
                    final String columnUniqueIdentifier,
                    final String sortBy,
                    final SortType sortType);

    SortType getSortType(final String tableUniqueIdentifier, final String columnUniqueIdentifier);
}
