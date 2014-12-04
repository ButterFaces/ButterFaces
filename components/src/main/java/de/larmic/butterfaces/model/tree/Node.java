package de.larmic.butterfaces.model.tree;

import java.util.Collection;

/**
 * Created by larmic on 24.10.14.
 */
public interface Node<T> {

    /**
     * @return the node title.
     */
    String getTitle();

    /**
     * @return optional object data.
     */
    T getData();

    /**
     * Returns an optional display icon. Will be rendered in front of the node title. Value should contain a relative
     * path to the webapp folder.
     *
     * @return optional display icon
     */
    String getImageIcon();

    /**
     * Returns an optional display glyphicon. Will be rendered in front of the node title. If image icon is set although
     * glyphicon icon will be ignored.
     *
     * @return optional glyphicon icon
     */
    String getGlyphiconIcon();

    /**
     * @return true if children (existing or not) should be rendered in tree component.
     */
    boolean isLeaf();

    /**
     * @return true if node and all sub nodes are collapsed.
     */
    boolean isCollapsed();

    /**
     * Sets collapsed flag.
     *
     * @param collapsed flag
     */
    void setCollapsed(final boolean collapsed);

    /**
     * @return a list of child nodes. An empty list if no sub nodes exits.
     */
    Collection<Node> getSubNodes();

}