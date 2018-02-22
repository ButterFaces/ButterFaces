package org.butterfaces.model.table;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO used by {@link TableColumnVisibilityModel} to handle column visibility changes by user interaction.
 */
public class TableColumnVisibility {
    private final String tableIdentifier;
    private final List<String> visibleColumns = new ArrayList<>();
    private final List<String> invisibleColumns = new ArrayList<>();

    public TableColumnVisibility(final String tableIdentifier,
                                 final List<String> visibleColumns,
                                 final List<String> invisibleColumns) {
        this.tableIdentifier = tableIdentifier;
        this.visibleColumns.addAll(visibleColumns);
        this.invisibleColumns.addAll(invisibleColumns);
    }

    public String getTableIdentifier() {
        return tableIdentifier;
    }

    public List<String> getInvisibleColumns() {
        return invisibleColumns;
    }

    public List<String> getVisibleColumns() {
        return visibleColumns;
    }
}
