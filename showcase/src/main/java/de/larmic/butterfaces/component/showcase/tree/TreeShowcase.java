package de.larmic.butterfaces.component.showcase.tree;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.event.TreeNodeSelectionEvent;
import de.larmic.butterfaces.event.TreeNodeSelectionListener;
import de.larmic.butterfaces.model.tree.DefaultNodeImpl;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
@SuppressWarnings("serial")
public class TreeShowcase extends AbstractCodeShowcase implements Serializable, TreeNodeSelectionListener {

    public static final String FONT_AWESOME_MARKER = "font-awesome";

    private boolean hideRootNode = false;
    private SelectionAjaxType selectionAjaxType = SelectionAjaxType.AJAX;
    private TreeIconType selectedIconType = TreeIconType.IMAGE;
    private boolean allExpanded = true;

    private Node selectedNode;

    private String glyphicon;
    private String collapsingClass;
    private String expansionClass;

    public Node getTree() {
        final Node secondFirstChild = createNode("secondFirstChild", "resources/images/folder-16.png", "glyphicon-folder-open");
        secondFirstChild.getSubNodes().add(createNode("secondFirstFirstChild", "resources/images/excel-16.png", "glyphicon-film"));

        final Node firstChild = createNode("firstChild", "resources/images/excel-16.png", "glyphicon-film");
        final Node secondChild = createNode("secondChild", "resources/images/folder-16.png", "glyphicon-folder-open");
        if (!allExpanded) {
            secondChild.setCollapsed(true);
        }
        final Node secondThirdChild = createNode("secondThirdChild", "resources/images/folder-16.png", "glyphicon-folder-open");
        secondThirdChild.getSubNodes().add(createNode("thirdFirstChild", "resources/images/excel-16.png", "glyphicon-film"));
        secondThirdChild.getSubNodes().add(createNode("thirdSecondChild", "resources/images/word-16.png", "glyphicon-file"));
        secondThirdChild.getSubNodes().add(createNode("thirdThirdChild", "resources/images/ppt-16.png", "glyphicon-signal"));
        secondChild.getSubNodes().add(secondFirstChild);
        secondChild.getSubNodes().add(createNode("secondSecondChild", "resources/images/excel-16.png", "glyphicon-film"));
        secondChild.getSubNodes().add(secondThirdChild);
        secondChild.getSubNodes().add(createNode("secondFourthChild", "resources/images/excel-16.png", "glyphicon-film"));
        secondChild.getSubNodes().add(createNode("secondFifthChild", "resources/images/excel-16.png", "glyphicon-film"));

        final Node rootNode = createNode("rootNode", "resources/images/folder-16.png", "glyphicon-folder-open");
        rootNode.getSubNodes().add(firstChild);
        rootNode.getSubNodes().add(secondChild);
        rootNode.getSubNodes().add(createNode("thirdChild", "resources/images/excel-16.png", "glyphicon-film"));

        return rootNode;
    }


    @Override
    public void processValueChange(final TreeNodeSelectionEvent event) {
        selectedNode = event.getNewValue();
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        codeExamples.add(this.createXhtmlCodeExample());
        codeExamples.add(this.createMyBeanCodeExample());
    }

    private JavaCodeExample createMyBeanCodeExample() {
        final JavaCodeExample myBean = new JavaCodeExample("MyBean.java", "mybean", "tree.demo", "MyBean", true);

        if (selectionAjaxType != SelectionAjaxType.NONE) {
            myBean.addImport("import de.larmic.butterfaces.event.TreeNodeSelectionEvent");
            myBean.addImport("import de.larmic.butterfaces.event.TreeNodeSelectionListener");
        }
        myBean.addImport("import de.larmic.butterfaces.model.tree.Node");
        myBean.addImport("import de.larmic.butterfaces.model.tree.DefaultNodeImpl");
        myBean.addImport("import javax.faces.view.ViewScoped");
        myBean.addImport("import javax.inject.Named");

        if (selectionAjaxType != SelectionAjaxType.NONE) {
            myBean.addInterfaces("TreeNodeSelectionListener");
        }

        if (isAjaxRendered()) {
            myBean.appendInnerContent("    private Node selectedNode;\n");
        }
        myBean.appendInnerContent("    public Node getTreeModel() {");

        myBean.appendInnerContent("        final Node firstChild = new DefaultNodeImpl(\"firstChild\");");
        if (selectedIconType == TreeIconType.GLYPHICON) {
            myBean.appendInnerContent("        firstChild.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
        } else if (selectedIconType == TreeIconType.IMAGE) {
            myBean.appendInnerContent("        firstChild.setImageIcon(\"some/path/16.png\");");
        }
        myBean.appendInnerContent("        final Node secondChild = new DefaultNodeImpl(\"second\");");
        if (!allExpanded) {
            myBean.appendInnerContent("        secondChild.setCollapsed(true);");
        }
        if (selectedIconType == TreeIconType.GLYPHICON) {
            myBean.appendInnerContent("        secondChild.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
        } else if (selectedIconType == TreeIconType.IMAGE) {
            myBean.appendInnerContent("        secondChild.setImageIcon(\"some/path/16.png\");");
        }
        myBean.appendInnerContent("        secondChild.getSubNodes().add(new DefaultNodeImpl(\"...\"))");
        myBean.appendInnerContent("        ...");
        myBean.appendInnerContent("        final Node rootNode = new DefaultNodeImpl(\"rootNode\");");
        if (selectedIconType == TreeIconType.IMAGE) {
            myBean.appendInnerContent("        rootNode.setImageIcon(\"some/path/16.png\");");
        } else if (selectedIconType == TreeIconType.GLYPHICON) {
            myBean.appendInnerContent("        rootNode.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
        }
        myBean.appendInnerContent("        rootNode.getSubNodes().add(firstChild);");
        myBean.appendInnerContent("        rootNode.getSubNodes().add(secondChild);");
        myBean.appendInnerContent("        return rootNode;");
        myBean.appendInnerContent("    }\n");
        if (isAjaxRendered()) {
            myBean.appendInnerContent("    @Override");
            myBean.appendInnerContent("    public void processValueChange(final TreeNodeSelectionEvent event) {");
            myBean.appendInnerContent("        selectedNode = event.getNewValue();");
            myBean.appendInnerContent("    }\n");
            myBean.appendInnerContent("    public Node getSelectedNode() {");
            myBean.appendInnerContent("        return selectedNode;");
            myBean.appendInnerContent("    }");
        }

        return myBean;
    }

    public List<SelectItem> getAjaxSelectionTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final SelectionAjaxType type : SelectionAjaxType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getIconTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final TreeIconType type : TreeIconType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getGlyphicons() {
        final List<SelectItem> items = new ArrayList<>();

        items.add(new SelectItem("bootstrap", "Butterfaces default"));
        items.add(new SelectItem("other-bootstrap", "other Bootstrap example"));
        items.add(new SelectItem(FONT_AWESOME_MARKER, "Font-Awesome example"));

        return items;
    }

    private XhtmlCodeExample createXhtmlCodeExample() {
        final boolean useFontAwesome = this.getGlyphicon() != null && FONT_AWESOME_MARKER.equals(this.getGlyphicon());

        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(useFontAwesome);

        xhtmlCodeExample.appendInnerContent("        <b:tree id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                value=\"#{myBean.treeModel}\"");
        if (isAjaxRendered()) {
            xhtmlCodeExample.appendInnerContent("                nodeSelectionListener=\"#{myBean}\"");
        }
        xhtmlCodeExample.appendInnerContent("                collapsingClass=\"" + this.getExpansionClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                expansionClass=\"" + this.getCollapsingClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                hideRootNode=\"" + this.isHideRootNode() + "\"");
        xhtmlCodeExample.appendInnerContent("                rendered=\"" + this.isRendered() + "\">");

        if (isAjaxRendered()) {
            if (isAjaxDisabled()) {
                xhtmlCodeExample.appendInnerContent("            <f:ajax render=\"nodeTitle\" disabled=\"true\"/>");
            } else {
                xhtmlCodeExample.appendInnerContent("            <f:ajax render=\"nodeTitle\"/>");
            }
        }

        xhtmlCodeExample.appendInnerContent("        </b:tree>");

        if (isAjaxRendered()) {
            xhtmlCodeExample.appendInnerContent("\n    <h:panelGroup id=\"nodeTitle\">");
            xhtmlCodeExample.appendInnerContent("            <h:output value=\"#{myBean.selectedNode.title}\"");
            xhtmlCodeExample.appendInnerContent("                      rendered=\"#{not empty myBean.selectedNode}\"/>");
            xhtmlCodeExample.appendInnerContent("    <h:panelGroup/>");
        }

        return xhtmlCodeExample;
    }

    private DefaultNodeImpl createNode(final String title, final String icon, final String glyphicon) {
        if (selectedIconType == TreeIconType.IMAGE) {
            return new DefaultNodeImpl(title, null, icon);
        } else if (selectedIconType == TreeIconType.GLYPHICON) {
            final DefaultNodeImpl node = new DefaultNodeImpl(title);
            node.setGlyphiconIcon("glyphicon " + glyphicon);
            return node;
        }

        return new DefaultNodeImpl(title);
    }

    public String getCollapsingClass() {
        return collapsingClass;
    }

    public String getExpansionClass() {
        return expansionClass;
    }

    public String getGlyphicon() {
        return glyphicon;
    }

    public void setGlyphicon(final String glyphicon) {
        this.glyphicon = glyphicon;

        switch (glyphicon) {
            case "bootstrap":
                collapsingClass = null;
                expansionClass = null;
                break;
            case "other-bootstrap":
                collapsingClass = "glyphicon glyphicon-resize-small";
                expansionClass = "glyphicon glyphicon-resize-full";
                break;
            case FONT_AWESOME_MARKER:
                collapsingClass = "fa fa-minus-square-o";
                expansionClass = "fa fa-plus-square-o";
                break;
        }
    }

    public boolean isHideRootNode() {
        return hideRootNode;
    }

    public void setHideRootNode(boolean hideRootNode) {
        this.hideRootNode = hideRootNode;
    }

    public boolean isAjaxRendered() {
        return SelectionAjaxType.NONE != selectionAjaxType;
    }

    public boolean isAjaxDisabled() {
        return SelectionAjaxType.AJAX_DISABLED == selectionAjaxType;
    }

    public SelectionAjaxType getSelectionAjaxType() {
        return selectionAjaxType;
    }

    public void setSelectionAjaxType(SelectionAjaxType selectionAjaxType) {
        this.selectionAjaxType = selectionAjaxType;
    }

    public Node getSelectedNode() {
        return selectedNode;
    }

    public boolean isAllExpanded() {
        return allExpanded;
    }

    public void setAllExpanded(boolean allExpanded) {
        this.allExpanded = allExpanded;
    }

    public TreeIconType getSelectedIconType() {
        return selectedIconType;
    }

    public void setSelectedIconType(TreeIconType selectedIconType) {
        this.selectedIconType = selectedIconType;
    }
}
