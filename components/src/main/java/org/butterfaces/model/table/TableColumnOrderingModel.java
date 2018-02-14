package org.butterfaces.model.table;

/**
 * Table model to handle column orderings (left and right). If model is not used it is not possible to change column
 * ordering by user interaction.
 */
public interface TableColumnOrderingModel {

    /**
     * Updates table column ordering. At all times it contains complete ordering information.
     * @param ordering {@link TableColumnOrdering}
     */
    void update(final TableColumnOrdering ordering);

    /**
     * @param tableUniqueIdentifier  table unique identifier (component client if if unique identifier is empty)
     * @param columnUniqueIdentifier column unique identifier (component client if if unique identifier is empty)
     * @return null if column identifier is not known, table identifier is not matching or no ordering is set.
     */
    Integer getOrderPosition(final String tableUniqueIdentifier, final String columnUniqueIdentifier);


}
