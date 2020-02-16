package org.butterfaces.component.renderkit.html_basic.text.part;

import org.butterfaces.model.tree.DefaultNodeImpl;
import org.butterfaces.model.tree.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class TrivialComponentsEntriesNodePartRendererTest {

    private List<Node> nodeList;
    private Map<Integer, Node> nodeMap;

    @BeforeEach
    void setUp() {
        nodeList = new ArrayList<>();
        nodeList.add(createNode("unit-test-node-name-1", "unit-test-node-description-1"));
        nodeList.add(createNode("unit-test-node-name-2</script>", "unit-test-node-description-2"));
        nodeList.add(createNode("unit-test-node-name-3</script", "unit-test-node-description-3"));

        nodeMap = new HashMap<>();
        nodeMap.put(0, createNode("unit-test-node-name-4", "unit-test-node-description-4"));
        nodeMap.put(1, createNode("unit-test-node-name-5", "unit-test-node-description-5"));
        nodeMap.put(2, createNode("unit-test-node-name-6", "unit-test-node-description-6"));
    }

    @Test
    void testRenderEntriesAsJSONNodesWithScriptEndTag() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
            new TrivialComponentsEntriesNodePartRenderer().renderEntriesAsJSON(nodeList, Collections.<String>emptyList(), nodeMap));

        assertThat(stringBuilder.toString().contains("</script>")).isFalse();
        assertThat(stringBuilder.toString().contains("</script")).isFalse();
    }

    private DefaultNodeImpl createNode(final String title, final String description) {
        return new DefaultNodeImpl(title) {
            @Override
            public String getDescription() {
                return description;
            }
        };
    }
}