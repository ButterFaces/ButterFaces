package de.larmic.butterfaces.model.tree;

import java.util.Collection;

/**
 * Created by larmic on 24.10.14.
 */
public interface Node<T> {

    String getTitle();
    T getData();
    String getIcon();
    boolean isLeaf();
    Collection<Node> getSubNodes();

}
