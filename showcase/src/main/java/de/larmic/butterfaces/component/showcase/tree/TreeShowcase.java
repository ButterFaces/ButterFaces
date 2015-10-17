package de.larmic.butterfaces.component.showcase.tree;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.event.TreeNodeExpansionListener;
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

@Named
@ViewScoped
@SuppressWarnings("serial")
public class TreeShowcase extends AbstractCodeShowcase implements Serializable, TreeNodeSelectionListener, TreeNodeExpansionListener {

    private boolean hideRootNode = false;
    private SelectionAjaxType selectionAjaxType = SelectionAjaxType.AJAX;
    private TreeIconType selectedIconType = TreeIconType.IMAGE;
    private TreeSearchBarModeType selectedSearchBarModeType = TreeSearchBarModeType.ALWAYS_VISIBLE;
    private boolean allExpanded = false;
    private String placeholder = "Search...";

    private Node selectedNode;

    public Node getTree() {
        final Node inbox = createNode("Inbox", "resources/images/arrow-down.png", "glyphicon-download", "43 unread");
        final Node drafts = createNode("Drafts", "resources/images/compose.png", "glyphicon-edit", "5");
        final Node sent = createNode("Sent", "resources/images/arrow-up.png", "glyphicon-send", "529 sent, 1 sending");

        final Node tagged = createNode("Tagged", "resources/images/shop.png", "glyphicon-tag", "9 unread");
        tagged.setCollapsed(true);
        tagged.getSubNodes().add(createNode("Important", "resources/images/shop.png", "glyphicon-tag", null));
        tagged.getSubNodes().add(createNode("Private", "resources/images/shop.png", "glyphicon-tag", null));

        final Node folders = createNode("Folders", "resources/images/folder.png", "glyphicon-folder-open", "27 files");
        folders.setCollapsed(true);
        folders.getSubNodes().add(createNode("Office", "resources/images/folder.png", "glyphicon-folder-open", "13 files"));
        folders.getSubNodes().add(createNode("Building", "resources/images/folder.png", "glyphicon-folder-open", "2 files"));
        folders.getSubNodes().add(createNode("Bills", "resources/images/folder.png", "glyphicon-folder-open", "12 files"));
        final Node trash = createNode("Trash", "resources/images/recycle.png", "glyphicon-trash", "7293 kB");

        final Node mail = createNode("Mail", "resources/images/mail.png", "glyphicon-envelope", "43 unread");
        mail.getSubNodes().add(inbox);
        mail.getSubNodes().add(drafts);
        mail.getSubNodes().add(sent);
        mail.getSubNodes().add(tagged);
        mail.getSubNodes().add(folders);
        mail.getSubNodes().add(trash);

        if (allExpanded) {
            tagged.setCollapsed(false);
            folders.setCollapsed(false);
        }

        final Node rootNode = createNode("rootNode", "resources/images/folder.png", "glyphicon-folder-open", "Project X");
        rootNode.getSubNodes().add(mail);

        return rootNode;
    }


    @Override
    public void processValueChange(final TreeNodeSelectionEvent event) {
        selectedNode = event.getNewValue();
    }

    @Override
    public boolean isValueSelected(Node data) {
        return selectedNode != null && data.getTitle().equals(selectedNode.getTitle());
    }

    @Override
    public void expandNode(Node node) {

    }

    @Override
    public void collapseNode(Node node) {

    }

    @Override
    public boolean isValueExpanded(Node data) {
        return false;
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
        myBean.appendInnerContent("        firstChild.setDescription(\"23 unread\");");
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
            myBean.appendInnerContent("    public void processTableSelection(final TreeNodeSelectionEvent event) {");
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

    public List<SelectItem> getSeachBarModeTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final TreeSearchBarModeType type : TreeSearchBarModeType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    private XhtmlCodeExample createXhtmlCodeExample() {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:tree id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                value=\"#{myBean.treeModel}\"");
        if (isAjaxRendered()) {
            xhtmlCodeExample.appendInnerContent("                nodeSelectionListener=\"#{myBean}\"");
        }
        xhtmlCodeExample.appendInnerContent("                hideRootNode=\"" + hideRootNode + "\"");
        xhtmlCodeExample.appendInnerContent("                searchBarMode=\"" + selectedSearchBarModeType.label + "\"");
        xhtmlCodeExample.appendInnerContent("                placeholder=\"" + placeholder + "\"");
        xhtmlCodeExample.appendInnerContent("                rendered=\"" + this.isRendered() + "\">");

        if (isAjaxRendered()) {
            if (isAjaxDisabled()) {
                xhtmlCodeExample.appendInnerContent("            <!-- use toggle to activate selection listener -->");
                xhtmlCodeExample.appendInnerContent("            <f:ajax event=\"click\" render=\"nodeTitle\" disabled=\"true\"/>");
                xhtmlCodeExample.appendInnerContent("            <!-- use toggle to activate expansion listener -->");
                xhtmlCodeExample.appendInnerContent("            <f:ajax event=\"toggle\" render=\"nodeTitle\" disabled=\"true\"/>");
            } else {
                xhtmlCodeExample.appendInnerContent("            <!-- use toggle to activate selection listener -->");
                xhtmlCodeExample.appendInnerContent("            <f:ajax event=\"click\" render=\"nodeTitle\"/>");
                xhtmlCodeExample.appendInnerContent("            <!-- use toggle to activate expansion listener -->");
                xhtmlCodeExample.appendInnerContent("            <f:ajax event=\"toggle\" render=\"nodeTitle\"/>");
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

    private DefaultNodeImpl createNode(final String title, final String icon, final String glyphicon, final String description) {
        final DefaultNodeImpl node = new DefaultNodeImpl(title);
        node.setDescription(description);
        if (selectedIconType == TreeIconType.IMAGE) {
            node.setImageIcon(icon);
            node.setDescription(description);
        } else if (selectedIconType == TreeIconType.GLYPHICON) {
            node.setGlyphiconIcon("glyphicon " + glyphicon);
        }

        return node;
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

    public TreeSearchBarModeType getSelectedSearchBarModeType() {
        return selectedSearchBarModeType;
    }

    public void setSelectedSearchBarModeType(TreeSearchBarModeType selectedSearchBarModeType) {
        this.selectedSearchBarModeType = selectedSearchBarModeType;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
