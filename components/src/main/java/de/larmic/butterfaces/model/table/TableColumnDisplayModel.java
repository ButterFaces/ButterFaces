package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public interface TableColumnDisplayModel {

    void showColumn(final String tableUniqueIdentifier, final String columnUniqueIdentifier);
    void hideColumn(final String tableUniqueIdentifier, final String columnUniqueIdentifier);

    /**
     * @return null if column id is not known.
     */
    Boolean isColumnHidden(final String tableUniqueIdentifier, final String columnUniqueIdentifier);
}
