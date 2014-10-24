package de.larmic.butterfaces.model.tree;

import java.util.Collection;

/**
 * Created by larmic on 24.10.14.
 */
public interface Node {

    String getTitle();
    boolean isRootNode();
    boolean isLeaf();
    Collection<Node> getSubNodes();

}
