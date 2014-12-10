package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public interface TableModel {

    void showColumn(final String columnClientId);
    void hideColumn(final String columnClientId);

    void sortColumn(final String columnClientId, final SortType sortType);

    /**
     * @return null if column client id is not known.
     */
    Boolean isColumnHidden(final String columnClientId);

    SortType getSortType(final String columnClientId);
}
