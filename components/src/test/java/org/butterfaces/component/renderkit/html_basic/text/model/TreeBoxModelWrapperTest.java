/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.renderkit.html_basic.text.model;

import org.butterfaces.component.html.text.HtmlTreeBox;
import org.butterfaces.model.tree.DefaultNodeImpl;
import org.butterfaces.component.html.text.HtmlTreeBox;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Lars Michaelis
 */
public class TreeBoxModelWrapperTest {

    @Test
    public void testCreateWrapperWithNodes() throws Exception {
        final HtmlTreeBox treeBox = new HtmlTreeBox();
        treeBox.setValues(Arrays.asList(new DefaultNodeImpl<>("demo1"), new DefaultNodeImpl<>("demo2"), new DefaultNodeImpl<>("demo3")));

        final TreeBoxModelWrapper treeBoxModelWrapper = new TreeBoxModelWrapper(treeBox);

        assertThat(treeBoxModelWrapper.getTreeBoxModelType()).isEqualTo(TreeBoxModelType.NODES);
        assertThat(treeBoxModelWrapper.getNodes()).extracting("title").containsExactly("demo1", "demo2", "demo3");
    }

    @Test
    public void testCreateWrapperWithNodesAndStrings() throws Exception {
        final HtmlTreeBox treeBox = new HtmlTreeBox();
        treeBox.setValues(Arrays.asList("demo1", new DefaultNodeImpl<>("demo2"), "demo3"));

        final TreeBoxModelWrapper treeBoxModelWrapper = new TreeBoxModelWrapper(treeBox);

        assertThat(treeBoxModelWrapper.getTreeBoxModelType()).isEqualTo(TreeBoxModelType.OBJECTS);
        assertThat(treeBoxModelWrapper.getNodes()).extracting("title").containsExactly("demo1", "demo2", "demo3");
    }

    @Test
    public void testCreateWrapperWithObjects() throws Exception {
        final HtmlTreeBox treeBox = new HtmlTreeBox();
        treeBox.setValues(Arrays.asList(new SimpleObject("demo1"), new SimpleObject("demo2"), new SimpleObject("demo3")));

        final TreeBoxModelWrapper treeBoxModelWrapper = new TreeBoxModelWrapper(treeBox);

        assertThat(treeBoxModelWrapper.getTreeBoxModelType()).isEqualTo(TreeBoxModelType.OBJECTS);
        assertThat(treeBoxModelWrapper.getNodes()).extracting("data.value").containsExactly("demo1", "demo2", "demo3");
    }

    @Test
    public void testCreateWrapperWithObjectsAndNodes() throws Exception {
        final HtmlTreeBox treeBox = new HtmlTreeBox();
        treeBox.setValues(Arrays.asList(new DefaultNodeImpl<>("demo1", new SimpleObject("demo1")), new SimpleObject("demo2"), new SimpleObject("demo3")));

        final TreeBoxModelWrapper treeBoxModelWrapper = new TreeBoxModelWrapper(treeBox);

        assertThat(treeBoxModelWrapper.getTreeBoxModelType()).isEqualTo(TreeBoxModelType.OBJECTS);
        assertThat(treeBoxModelWrapper.getNodes()).extracting("data.value").containsExactly("demo1", "demo2", "demo3");
    }

    private class SimpleObject {
        private final String value;

        private SimpleObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}