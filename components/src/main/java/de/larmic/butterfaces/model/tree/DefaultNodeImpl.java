package de.larmic.butterfaces.model.tree;

import java.util.Collection;

/**
 * Created by larmic on 24.10.14.
 */
public class DefaultNodeImpl implements Node {

    private final String title;

    public DefaultNodeImpl(final String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean isRootNode() {
        return false;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Collection<Node> getSubNodes() {
        return null;
    }
}
