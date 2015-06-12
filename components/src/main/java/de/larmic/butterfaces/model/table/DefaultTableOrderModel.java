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
    public void orderColumnToLeft(final String tableUniqueIdentifier,
                                  final String columnUniqueIdentifier) {
        final Integer nullableActualOrderPosition = getOrderPosition(tableUniqueIdentifier, columnUniqueIdentifier);

        if (nullableActualOrderPosition == null) {
            ordering.add(columnUniqueIdentifier);
        } else if (nullableActualOrderPosition > 0) {
            Collections.swap(ordering, nullableActualOrderPosition, nullableActualOrderPosition - 1);
        }
    }

    @Override
    public void orderColumnToRight(final String tableUniqueIdentifier,
                                   final String columnUniqueIdentifier) {
        final Integer nullableActualOrderPosition = getOrderPosition(tableUniqueIdentifier, columnUniqueIdentifier);

        if (nullableActualOrderPosition == null) {
            ordering.add(columnUniqueIdentifier);
        } else if (nullableActualOrderPosition < ordering.size()-1) {
            Collections.swap(ordering, nullableActualOrderPosition, nullableActualOrderPosition + 1);
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
