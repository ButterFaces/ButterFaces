package de.larmic.butterfaces.model.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by larmic on 24.10.14.
 */
public class DefaultNodeImpl implements Node {

    private final List<Node> subNodes = new ArrayList<>();
    private final String title;

    public DefaultNodeImpl(final String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean isLeaf() {
        return this.subNodes.isEmpty();
    }

    @Override
    public Collection<Node> getSubNodes() {
        return subNodes;
    }
}
