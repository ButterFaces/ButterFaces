package de.larmic.butterfaces.event;

/**
 * Handle single tree node selection on {@link de.larmic.butterfaces.component.html.tree.HtmlTree} component if registered.
 */
public interface TreeNodeSelectionListener {

    /**
     * Called if a single click is processed by user interaction.
     */
    void processValueChange(final TreeNodeSelectionEvent event);

}
