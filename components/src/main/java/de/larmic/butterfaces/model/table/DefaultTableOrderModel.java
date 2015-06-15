package de.larmic.butterfaces.model.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by larmic on 10.06.15.
 */
public class DefaultTableOrderModel implements TableOrderModel {

    protected final List<String> ordering = new ArrayList<>();

    @Override
    public void orderColumnToPosition(final String tableUniqueIdentifier,
                                      final String columnUniqueIdentifier,
                                      final int position) {
        final Integer nullableActualOrderPosition = getOrderPosition(tableUniqueIdentifier, columnUniqueIdentifier);

        if (nullableActualOrderPosition == null) {
            ordering.add(columnUniqueIdentifier);
        } else if (position >= 0 && position < ordering.size()) {
            Collections.swap(ordering, nullableActualOrderPosition, position);
        }
    }

    @Override
    public Integer getOrderPosition(final String tableUniqueIdentifier, final String columnUniqueIdentifier) {
        int index = 0;

        for (String uniqueIdentifier : ordering) {
            if (columnUniqueIdentifier.equals(uniqueIdentifier)) {
                return index;
            }

            index++;
        }

        return null;
    }
}
