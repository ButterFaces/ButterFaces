package de.larmic.butterfaces.model.table;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by larmic on 03.12.14.
 */
public class DefaultColumnDisplayTableModel implements TableColumnDisplayModel {

    /**
     * Map if column client id and hide information.
     */
    private final Map<String, Boolean> columnInformation = new HashMap<>();

    @Override
    public void showColumn(final String tableUniqueIdentifier, final String columnUniqueIdentifier) {
        columnInformation.put(columnUniqueIdentifier, false);
    }

    @Override
    public void hideColumn(final String tableUniqueIdentifier, final String columnUniqueIdentifier) {
        columnInformation.put(columnUniqueIdentifier, true);
    }


    @Override
    public Boolean isColumnHidden(final String tableUniqueIdentifier, final String columnUniqueIdentifier) {
        final Boolean hideColumn = columnInformation.get(columnUniqueIdentifier);
        return hideColumn == null ? null : hideColumn;
    }
}
