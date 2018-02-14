package org.butterfaces.model.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 24.10.14.
 */
public class DefaultNodeImpl<T> implements Node<T> {

    private final List<Node> subNodes = new ArrayList<>();
    private T data;
    private String title;
    private String description;
    private String imageIcon;
    private String glyphiconIcon;
    private String styleClass;
    private boolean isCollapsed;

    public DefaultNodeImpl() {
    }

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

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public List<Node> getSubNodes() {
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

    @Override
    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public void setData(T data) {
        this.data = data;
    }
}
