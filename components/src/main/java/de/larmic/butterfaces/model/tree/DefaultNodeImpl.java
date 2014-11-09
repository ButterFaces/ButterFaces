package de.larmic.butterfaces.model.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by larmic on 24.10.14.
 */
public class DefaultNodeImpl<T> implements Node<T> {

    private final List<Node> subNodes = new ArrayList<>();
    private final String title;
    private final T data;
    private final String iconPath;

    public DefaultNodeImpl(final String title) {
        this(title, null, null);
    }

    public DefaultNodeImpl(final String title, final T data) {
        this(title, data, null);
    }

    public DefaultNodeImpl(final String title, final T data, final String iconPath) {
        this.title = title;
        this.data = data;
        this.iconPath = iconPath;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public String getIcon() {
        return iconPath;
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
