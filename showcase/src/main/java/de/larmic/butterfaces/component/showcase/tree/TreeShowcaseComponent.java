package de.larmic.butterfaces.component.showcase.tree;

import de.larmic.butterfaces.component.showcase.AbstractShowcaseComponent;
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
public class TreeShowcaseComponent extends AbstractShowcaseComponent implements Serializable, TreeNodeSelectionListener {

    public static final String FONT_AWESOME_MARKER = "font-awesome";

    private boolean hideRootNode = false;
    private TreeSelectionAjaxType selectionAjaxType = TreeSelectionAjaxType.AJAX;
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
    protected void addJavaCode(final StringBuilder sb) {
        sb.append("package de.larmic.tree,demo;\n\n");

        if (selectionAjaxType != TreeSelectionAjaxType.NONE) {
            sb.append("import de.larmic.butterfaces.event.TreeNodeSelectionEvent;\n");
            sb.append("import de.larmic.butterfaces.event.TreeNodeSelectionListener;\n");
        }
        sb.append("import de.larmic.butterfaces.model.tree.Node;\n");
        sb.append("import de.larmic.butterfaces.model.tree.DefaultNodeImpl;\n\n");
        sb.append("import javax.faces.view.ViewScoped;\n");
        sb.append("import javax.inject.Named;\n\n");

        sb.append("@ViewScoped\n");
        sb.append("@Named\n");
        if (selectionAjaxType != TreeSelectionAjaxType.NONE) {
            sb.append("public class MyBean implements Serializable, TreeNodeSelectionListener {\n\n");
        } else {
            sb.append("public class MyBean implements Serializable {\n\n");
        }
        if (isAjaxRendered()) {
            sb.append("    private Node selectedNode;\n\n");
        }
        sb.append("    public Node getTreeModel() {\n");

        sb.append("        final Node firstChild = new DefaultNodeImpl(\"firstChild\");\n");
        if (selectedIconType == TreeIconType.GLYPHICON) {
            sb.append("        firstChild.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");\n");
        } else if (selectedIconType == TreeIconType.IMAGE) {
            sb.append("        firstChild.setImageIcon(\"some/path/16.png\");\n");
        }
        sb.append("        final Node secondChild = new DefaultNodeImpl(\"second\");\n");
        if (!allExpanded) {
            sb.append("        secondChild.setCollapsed(true);\n");
        }
        if (selectedIconType == TreeIconType.GLYPHICON) {
            sb.append("        secondChild.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");\n");
        } else if (selectedIconType == TreeIconType.IMAGE) {
            sb.append("        secondChild.setImageIcon(\"some/path/16.png\");\n");
        }
        sb.append("        secondChild.getSubNodes().add(new DefaultNodeImpl(\"...\"))\n");
        sb.append("        ...\n");
        sb.append("        final Node rootNode = new DefaultNodeImpl(\"rootNode\");\n");
        if (selectedIconType == TreeIconType.IMAGE) {
            sb.append("        rootNode.setImageIcon(\"some/path/16.png\");\n");
        } else if (selectedIconType == TreeIconType.GLYPHICON) {
            sb.append("        rootNode.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");\n");
        }
        sb.append("        rootNode.getSubNodes().add(firstChild);\n");
        sb.append("        rootNode.getSubNodes().add(secondChild);\n");
        sb.append("        return rootNode;\n");
        sb.append("    }\n\n");
        if (isAjaxRendered()) {
            sb.append("    @Override\n");
            sb.append("    public void processValueChange(final TreeNodeSelectionEvent event) {\n");
            sb.append("        selectedNode = event.getNewValue();\n");
            sb.append("    }\n\n");
            sb.append("    public Node getSelectedNode() {\n");
            sb.append("        return selectedNode;\n");
            sb.append("    }\n\n");
        }
        sb.append("}");
    }

    public List<SelectItem> getAjaxSelectionTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final TreeSelectionAjaxType type : TreeSelectionAjaxType.values()) {
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

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        if (this.getGlyphicon() != null && FONT_AWESOME_MARKER.equals(this.getGlyphicon())) {
            this.addXhtmlStart(sb, "<h:head>\n    <link href=\"//maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css\"\n          rel=\"stylesheet\">\n</h:head>");
        } else {
            this.addXhtmlStart(sb);
        }

        sb.append("        <b:tree id=\"input\"\n");

        this.appendString("value", "#{myBean.treeModel}", sb);
        if (isAjaxRendered()) {
            this.appendString("nodeSelectionListener", "#{myBean}", sb);
        }
        this.appendString("collapsingClass", this.getCollapsingClass(), sb);
        this.appendString("expansionClass", this.getExpansionClass(), sb);
        this.appendBoolean("hideRootNode", this.isHideRootNode(), sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        if (isAjaxRendered()) {
            if (isAjaxDisabled()) {
                sb.append("            <f:ajax render=\"nodeTitle\" disabled=\"true\"/>\n");
            } else {
                sb.append("            <f:ajax render=\"nodeTitle\"/>\n");
            }
        }

        sb.append("        </b:tree>");

        if (isAjaxRendered()) {
            sb.append("\n\n        <h:panelGroup id=\"nodeTitle\">\n");
            sb.append("            <h:output value=\"#{myBean.selectedNode.title}\"\n");
            sb.append("                      rendered=\"#{not empty myBean.selectedNode}\"/>\n");
            sb.append("        <h:panelGroup/>");
        }

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                ";
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
        return TreeSelectionAjaxType.NONE != selectionAjaxType;
    }

    public boolean isAjaxDisabled() {
        return TreeSelectionAjaxType.AJAX_DISABLED == selectionAjaxType;
    }

    public TreeSelectionAjaxType getSelectionAjaxType() {
        return selectionAjaxType;
    }

    public void setSelectionAjaxType(TreeSelectionAjaxType selectionAjaxType) {
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
