package de.larmic.butterfaces.model.table;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by larmic on 03.12.14.
 */
public class DefaultTableModel implements TableModel {

    /**
     * Map if column client id and hide information.
     */
    private final Map<String, Boolean> columnInformation = new HashMap<>();
    private final Map<String, SortType> columnSortTypes = new HashMap<>();

    @Override
    public void showColumn(final String columnClientId) {
        columnInformation.put(columnClientId, false);
    }

    @Override
    public void hideColumn(final String columnClientId) {
        columnInformation.put(columnClientId, true);
    }

    @Override
    public void sortColumn(final String columnClientId, final SortType sortType) {
        columnSortTypes.put(columnClientId, sortType);
    }

    @Override
    public Boolean isColumnHidden(final String columnClientId) {
        final Boolean hideColumn = columnInformation.get(columnClientId);
        return hideColumn == null ? null : hideColumn;
    }

    @Override
    public SortType getSortType(final String columnClientId) {
        return columnSortTypes.get(columnClientId);
    }
}
