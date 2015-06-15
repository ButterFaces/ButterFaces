package de.larmic.butterfaces.model.table;

/**
 * Created by larmic on 03.12.14.
 */
public interface TableOrderModel {

    void orderColumnToPosition(final String tableUniqueIdentifier,
                               final String columnUniqueIdentifier,
                               final int position);

    /**
     * @return null if order position is unknown.
     */
    Integer getOrderPosition(final String tableUniqueIdentifier, final String columnUniqueIdentifier);
}
