package de.larmic.butterfaces.event;

import de.larmic.butterfaces.model.tree.Node;

/**
 * Handle single tree node selection on {@link de.larmic.butterfaces.component.html.tree.HtmlTree} component if registered.
 */
public interface TreeNodeSelectionListener {

    /**
     * Called if a single click is processed by user interaction.
     *
     * @param event corresponding {@link TreeNodeSelectionEvent}
     */
    void processValueChange(final TreeNodeSelectionEvent event);

    /**
     * @return true if actual value is selected. if a selected row is found remaining rows will not be checked because
     * only single tree node selection is available.
     *
     * @param data selected row data
     */
    boolean isValueSelected(final Node data);
}
