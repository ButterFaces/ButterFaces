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
    private String imageIcon;
    private String glyphiconIcon;
    private boolean isCollapsed;

    public DefaultNodeImpl(final String title) {
        this(title, null, null);
    }

    public DefaultNodeImpl(final String title, final T data) {
        this(title, data, null);
    }

    public DefaultNodeImpl(final String title, final T data, final String iconPath) {
        this.title = title;
        this.data = data;
        this.imageIcon = iconPath;
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
    public String getImageIcon() {
        return imageIcon;
    }

    @Override
    public String getGlyphiconIcon() {
        return glyphiconIcon;
    }

    public void setImageIcon(String imageIcon) {
        this.imageIcon = imageIcon;
    }


    public void setGlyphiconIcon(String glyphiconIcon) {
        this.glyphiconIcon = glyphiconIcon;
    }

    @Override
    public boolean isLeaf() {
        return this.subNodes.isEmpty();
    }

    @Override
    public Collection<Node> getSubNodes() {
        return subNodes;
    }

    @Override
    public boolean isCollapsed() {
        return isCollapsed;
    }

    @Override
    public void setCollapsed(boolean isCollapsed) {
        this.isCollapsed = isCollapsed;
    }
}
