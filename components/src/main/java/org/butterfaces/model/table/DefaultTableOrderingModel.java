package org.butterfaces.model.table;

/**
 * Created by larmic on 10.06.15.
 */
public class DefaultTableOrderingModel implements TableColumnOrderingModel {

    private TableColumnOrdering ordering;

    @Override
    public void update(final TableColumnOrdering ordering) {
        this.ordering = ordering;
    }

    @Override
    public Integer getOrderPosition(final String tableUniqueIdentifier, final String columnUniqueIdentifier) {
        if (ordering == null
                || !ordering.getTableIdentifier().equalsIgnoreCase(tableUniqueIdentifier)
                || !ordering.getOrderedColumnIdentifiers().contains(columnUniqueIdentifier)) {
            return null;
        }

        int index = 0;

        for (String uniqueIdentifier : ordering.getOrderedColumnIdentifiers()) {
            if (columnUniqueIdentifier.equals(uniqueIdentifier)) {
                return index;
            }

            index++;
        }

        throw new RuntimeException("Could not get order position of table " + tableUniqueIdentifier + " and column " + columnUniqueIdentifier);
    }
}
