package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public interface TableColumnDisplayModel {

    void showColumn(final String clientId);
    void hideColumn(final String clientId);

    /**
     * @return null if column id is not known.
     */
    Boolean isColumnHidden(final String clientId);
}
