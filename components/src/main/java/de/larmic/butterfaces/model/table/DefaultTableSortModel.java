package de.larmic.butterfaces.model.table;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by larmic on 03.12.14.
 */
public class DefaultTableSortModel implements TableSortModel {

    /**
     * Map of column id and {@link de.larmic.butterfaces.model.table.SortType} information.
     */
    private final Map<String, SortType> columnSortTypes = new HashMap<>();

    @Override
    public void sortColumn(final String columnId, final String sortBy, final SortType sortType) {
        columnSortTypes.put(columnId, sortType);
    }

    @Override
    public SortType getSortType(final String columnClientId) {
        return columnSortTypes.get(columnClientId);
    }
}
