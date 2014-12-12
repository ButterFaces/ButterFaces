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
    public void showColumn(final String clientId) {
        columnInformation.put(clientId, false);
    }

    @Override
    public void hideColumn(final String clientId) {
        columnInformation.put(clientId, true);
    }


    @Override
    public Boolean isColumnHidden(final String clientId) {
        final Boolean hideColumn = columnInformation.get(clientId);
        return hideColumn == null ? null : hideColumn;
    }
}
