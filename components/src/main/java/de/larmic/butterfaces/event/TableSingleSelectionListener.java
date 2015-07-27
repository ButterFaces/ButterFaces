package de.larmic.butterfaces.event;

/**
 * Implement interface to add single row selection to b:table component.
 */
public interface TableSingleSelectionListener<T> {

    /**
     * Will be called if table single selection is used and ajax is activated.
     *
     * @param data selected row data
     */
    void processTableSelection(final T data);

    /**
     * @return true if actual value is selected. if a selected row is found remaining rows will not be checked because
     * only single row selection is available.
     *
     * @param data selected row data
     */
    boolean isValueSelected(final T data);

}
