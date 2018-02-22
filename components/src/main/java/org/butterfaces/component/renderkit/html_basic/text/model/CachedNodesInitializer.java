/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.renderkit.html_basic.text.model;

import org.butterfaces.model.tree.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lars Michaelis
 */
public class CachedNodesInitializer {

    public static Map<Integer, Node> createNodesMap(final List<Node> nodes) {
        final Map<Integer, Node> nodesMap = new HashMap<>();
        initCachedNodes(nodes, nodesMap, 0);
        return nodesMap;
    }

    private static int initCachedNodes(final List<Node> nodes,
                                       final Map<Integer, Node> nodesMapToFill,
                                       final int index) {
        int newIndex = index;

        for (Node node : nodes) {
            nodesMapToFill.put(newIndex, node);
            newIndex++;

            if (node.getSubNodes().size() > 0) {
                newIndex = initCachedNodes(node.getSubNodes(), nodesMapToFill, newIndex);
            }
        }

        return newIndex;
    }

}
