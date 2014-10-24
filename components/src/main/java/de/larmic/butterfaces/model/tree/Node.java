package de.larmic.butterfaces.model.tree;

import java.util.Collection;

/**
 * Created by larmic on 24.10.14.
 */
public interface Node {

    String getTitle();
    boolean isLeaf();
    Collection<Node> getSubNodes();

}
