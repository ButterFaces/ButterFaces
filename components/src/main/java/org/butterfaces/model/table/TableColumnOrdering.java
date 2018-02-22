package org.butterfaces.model.table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 20.07.15.
 */
public class TableColumnOrdering {

    private final String tableIdentifier;
    private final List<String> orderedColumnIdentifiers = new ArrayList<>();

    public TableColumnOrdering(final String tableIdentifier, final List<String> columnIdentifier) {
        this.tableIdentifier = tableIdentifier;
        this.orderedColumnIdentifiers.addAll(columnIdentifier);
    }

    public String getTableIdentifier() {
        return tableIdentifier;
    }

    public List<String> getOrderedColumnIdentifiers() {
        return orderedColumnIdentifiers;
    }
}
