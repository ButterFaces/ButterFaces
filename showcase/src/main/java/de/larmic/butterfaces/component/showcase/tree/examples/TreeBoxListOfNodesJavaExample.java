/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.tree.examples;

import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.tree.ShowcaseTreeNode;
import de.larmic.butterfaces.component.showcase.tree.TreeIconType;
import de.larmic.butterfaces.component.showcase.tree.TreeTemplateType;

/**
 * @author Lars Michaelis
 */
public class TreeBoxListOfNodesJavaExample extends JavaCodeExample {

    public TreeBoxListOfNodesJavaExample(TreeTemplateType selectedTreeTemplateType, ShowcaseTreeNode showcaseTreeNode) {
        super("MyBean.java", "mybean", "treeBox.demo", "MyBean", true);

        this.addImport("import de.larmic.butterfaces.model.tree.Node");
        this.addImport("import de.larmic.butterfaces.model.tree.DefaultNodeImpl");
        this.addImport("import javax.faces.view.ViewScoped");
        this.addImport("import javax.inject.Named");

        this.appendInnerContent("    private List<Node> nodes = new ArrayList<Node>();\n");
        this.appendInnerContent("    public List<Node> getValues() {");
        this.appendInnerContent("        if (nodes.isEmpty()) {");
        if (selectedTreeTemplateType == TreeTemplateType.CUSTOM) {
            this.appendInnerContent("            final Node firstChild = new DefaultNodeImpl(\"firstChild\", new NodeData());");
        } else {
            this.appendInnerContent("            final Node firstChild = new DefaultNodeImpl(\"firstChild\");");
        }
        this.appendInnerContent("            firstChild.setDescription(\"23 unread\");");
        if (showcaseTreeNode.getSelectedIconType() == TreeIconType.GLYPHICON) {
            this.appendInnerContent("            firstChild.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
        } else if (showcaseTreeNode.getSelectedIconType() == TreeIconType.IMAGE) {
            this.appendInnerContent("            firstChild.setImageIcon(\"some/path/16.png\");");
        }
        if (selectedTreeTemplateType == TreeTemplateType.CUSTOM) {
            this.appendInnerContent("            final Node secondChild = new DefaultNodeImpl(\"second\", new NodeData());");
        } else {
            this.appendInnerContent("            final Node secondChild = new DefaultNodeImpl(\"second\");");
        }
        if (showcaseTreeNode.getSelectedIconType() == TreeIconType.GLYPHICON) {
            this.appendInnerContent("            secondChild.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
        } else if (showcaseTreeNode.getSelectedIconType() == TreeIconType.IMAGE) {
            this.appendInnerContent("            secondChild.setImageIcon(\"some/path/16.png\");");
        }
        if (selectedTreeTemplateType == TreeTemplateType.CUSTOM) {
            this.appendInnerContent("            secondChild.getSubNodes().add(new DefaultNodeImpl(\"...\"), new NodeData())");
        } else {
            this.appendInnerContent("            secondChild.getSubNodes().add(new DefaultNodeImpl(\"...\"))");
        }
        this.appendInnerContent("            ...");
        this.appendInnerContent("            nodes.add(firstChild);");
        this.appendInnerContent("            nodes.add(secondChild);");
        this.appendInnerContent("        }");
        this.appendInnerContent("        return nodes;");
        this.appendInnerContent("    }");
    }
}
