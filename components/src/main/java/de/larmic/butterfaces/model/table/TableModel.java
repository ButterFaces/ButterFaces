package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public interface TableModel {

    void showColumn(final String columnClientId);
    void hideColumn(final String columnClientId);

    /**
     * @return null if column client id is not known.
     */
    Boolean isHideColumn(final String columnClientId);

}
