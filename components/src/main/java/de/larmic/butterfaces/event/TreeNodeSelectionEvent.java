package de.larmic.butterfaces.event;

import de.larmic.butterfaces.model.tree.Node;

/**
 * Created by larmic on 06.11.14.
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
