/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.tree.examples;

import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.tree.ShowcaseTreeNode;
import org.butterfaces.component.showcase.tree.TreeBoxExampleType;
import org.butterfaces.component.showcase.tree.TreeIconType;
import org.butterfaces.component.showcase.tree.TreeTemplateType;
import org.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class TreeBoxRootNodeJavaExample extends JavaCodeExample {

    public TreeBoxRootNodeJavaExample(TreeBoxExampleType selectedTreeBoxExampleType, ShowcaseTreeNode showcaseTreeNode) {
        super("MyBean.java", "mybean", "treeBox.demo", "MyBean", true);

        this.addImport("import org.butterfaces.model.tree.Node");
        this.addImport("import org.butterfaces.model.tree.DefaultNodeImpl");
        this.addImport("import javax.faces.view.ViewScoped");
        this.addImport("import javax.inject.Named");

        this.appendInnerContent("    private Node rootNode;\n");
        this.appendInnerContent("    private Node selectedValue;\n");
        this.appendInnerContent("    public Node getValues() {");
        this.appendInnerContent("        if (rootNode == null) {");
        if (selectedTreeBoxExampleType == TreeBoxExampleType.TEMPLATE) {
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
        if (selectedTreeBoxExampleType == TreeBoxExampleType.TEMPLATE) {
            this.appendInnerContent("            final Node secondChild = new DefaultNodeImpl(\"second\", new NodeData());");
        } else {
            this.appendInnerContent("            final Node secondChild = new DefaultNodeImpl(\"second\");");
        }
        if (showcaseTreeNode.getSelectedIconType() == TreeIconType.GLYPHICON) {
            this.appendInnerContent("            secondChild.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
        } else if (showcaseTreeNode.getSelectedIconType() == TreeIconType.IMAGE) {
            this.appendInnerContent("            secondChild.setImageIcon(\"some/path/16.png\");");
        }
        if (selectedTreeBoxExampleType == TreeBoxExampleType.TEMPLATE) {
            this.appendInnerContent("            secondChild.getSubNodes().add(new DefaultNodeImpl(\"...\"), new NodeData())");
        } else {
            this.appendInnerContent("            secondChild.getSubNodes().add(new DefaultNodeImpl(\"...\"))");
        }
        this.appendInnerContent("            ...");
        if (selectedTreeBoxExampleType == TreeBoxExampleType.TEMPLATE) {
            this.appendInnerContent("            rootNode = new DefaultNodeImpl(\"rootNode\", new NodeData());");
        } else {
            this.appendInnerContent("            rootNode = new DefaultNodeImpl(\"rootNode\");");
        }
        if (showcaseTreeNode.getSelectedIconType() == TreeIconType.IMAGE) {
            this.appendInnerContent("            rootNode.setImageIcon(\"some/path/16.png\");");
        } else if (showcaseTreeNode.getSelectedIconType() == TreeIconType.GLYPHICON) {
            this.appendInnerContent("            rootNode.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
        }
        this.appendInnerContent("            rootNode.getSubNodes().add(firstChild);");
        this.appendInnerContent("            rootNode.getSubNodes().add(secondChild);");
        this.appendInnerContent("        }");
        this.appendInnerContent("        return rootNode;");
        this.appendInnerContent("    }");

        this.appendInnerContent("\n    // GETTER + SETTER (selectedValue)");
    }
}
