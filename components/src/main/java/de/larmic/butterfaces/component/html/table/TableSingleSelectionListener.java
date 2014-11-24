package de.larmic.butterfaces.component.html.table;

/**
 * Created by larmic on 21.11.14.
 */
public interface TableSingleSelectionListener<T> {

    /**
     * Will be called if table single selection is used and ajax is activated.
     *
     * @param data selected row data
     */
    public void processValueChange(final T data);

}
