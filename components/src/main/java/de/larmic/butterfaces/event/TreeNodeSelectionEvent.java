package de.larmic.butterfaces.event;

import de.larmic.butterfaces.model.tree.Node;

/**
 * {@link TreeNodeSelectionListener} corresponding event. Holding old value (is present) and new selected value.
 */
public class TreeNodeSelectionEvent {

    private final Node oldValue;
    private final Node newValue;

    public TreeNodeSelectionEvent(final Node oldValue, final Node newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public Node getOldValue() {
        return oldValue;
    }

    public Node getNewValue() {
        return newValue;
    }
}
