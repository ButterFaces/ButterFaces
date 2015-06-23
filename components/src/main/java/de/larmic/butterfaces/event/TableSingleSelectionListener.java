package de.larmic.butterfaces.event;

/**
 * Created by larmic on 21.11.14.
 */
public interface TableSingleSelectionListener<T> {

    /**
     * Will be called if table single selection is used and ajax is activated.
     *
     * @param data selected row data
     */
    public void processTableSelection(final T data);

    public boolean isValueSelected(final T data);

}
