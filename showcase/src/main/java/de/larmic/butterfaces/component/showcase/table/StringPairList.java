package de.larmic.butterfaces.component.showcase.table;

import de.larmic.butterfaces.component.html.table.TableSingleSelection;

import java.util.ArrayList;

/**
 * Created by larmic on 21.11.14.
 */
public class StringPairList<T> extends ArrayList<T> implements TableSingleSelection<T> {

    private T selectedValue;

    @Override
    public void updateSelection(final T data) {
        this.selectedValue = data;
    }

    public T getSelectedValue() {
        return selectedValue;
    }
}
