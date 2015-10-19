package de.larmic.butterfaces.event;

import de.larmic.butterfaces.model.tree.Node;

/**
 * Handle single tree node selection on {@link de.larmic.butterfaces.component.html.tree.HtmlTree} component if registered.
 */
public interface TreeNodeExpansionListener {

    /**
     * Called if a node is expanded by user interaction.
     *
     * @param node the expanding node
     */
    void expandNode(final Node node);

    /**
     * Called if a node is collapsed by user interaction.
     *
     * @param node the collapsing node
     */
    void collapseNode(final Node node);
}
