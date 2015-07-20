package de.larmic.butterfaces.model.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 20.07.15.
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
