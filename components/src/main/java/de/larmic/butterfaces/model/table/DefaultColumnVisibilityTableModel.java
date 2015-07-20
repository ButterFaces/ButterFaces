package de.larmic.butterfaces.model.table;

/**
 * Simple implementation of {@link TableColumnVisibilityModel}. Matches visibility by using column unique identifier.
 */
public class DefaultColumnVisibilityTableModel implements TableColumnVisibilityModel {

    private TableColumnVisibility visibility;

    @Override
    public void update(final TableColumnVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public Boolean isColumnHidden(final String tableUniqueIdentifier, final String columnUniqueIdentifier) {
        return visibility != null
                ? visibility.getTableIdentifier().equalsIgnoreCase(tableUniqueIdentifier) && visibility.getInvisibleColumns().contains(columnUniqueIdentifier)
                : null;
    }
}
