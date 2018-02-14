package org.butterfaces.component.showcase.tree;

import org.butterfaces.component.showcase.AbstractCodeShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.tree.examples.TreeBoxWebXmlExample;
import org.butterfaces.event.TreeNodeExpansionListener;
import org.butterfaces.event.TreeNodeSelectionEvent;
import org.butterfaces.event.TreeNodeSelectionListener;
import org.butterfaces.model.tree.DefaultNodeImpl;
import org.butterfaces.model.tree.EnumTreeBoxWrapper;
import org.butterfaces.model.tree.Node;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.tree.examples.TreeBoxWebXmlExample;
import org.butterfaces.event.TreeNodeExpansionListener;
import org.butterfaces.event.TreeNodeSelectionEvent;
import org.butterfaces.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class TreeShowcase extends AbstractCodeShowcase implements Serializable, TreeNodeSelectionListener, TreeNodeExpansionListener {

    private final ShowcaseTreeNode showcaseTreeNode = new ShowcaseTreeNode();
    private boolean hideRootNode = false;
    private SelectionAjaxType selectionAjaxType = SelectionAjaxType.AJAX;
    private TreeTemplateType selectedTreeTemplateType = TreeTemplateType.DEFAULT;
    private TreeSearchBarModeType selectedSearchBarModeType = TreeSearchBarModeType.ALWAYS_VISIBLE;
    private boolean allExpanded = false;
    private String placeholder = "Search...";
    private Integer toManyVisibleItemsRenderDelay;
    private Integer toManyVisibleItemsThreshold;
    private String noEntriesText;
    private String spinnerText;

    private Node selectedNode;

    @Override
    public void processValueChange(final TreeNodeSelectionEvent event) {
        this.selectedNode = event.getNewValue();
    }

    @Override
    public boolean isValueSelected(Node data) {
        return this.selectedNode != null && data.getTitle().equals(this.selectedNode.getTitle());
    }

    @Override
    public void expandNode(Node node) {

    }

    @Override
    public void collapseNode(Node node) {

    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        codeExamples.add(this.createXhtmlCodeExample());
        codeExamples.add(this.createMyBeanCodeExample());

        if (selectedTreeTemplateType == TreeTemplateType.CUSTOM) {
            codeExamples.add(this.createNodeDataCodeExample());
        }

        codeExamples.add(new TreeBoxWebXmlExample());

    }

    private JavaCodeExample createNodeDataCodeExample() {
        final JavaCodeExample codeExample = new JavaCodeExample("NodeData.java", "nodeData", "tree.demo", "NodeData", false);

        codeExample.appendInnerContent("    private final UUID uuid = UUID.randomUUID();\n");
        codeExample.appendInnerContent("    private final Date createDate = new Date();\n");
        codeExample.appendInnerContent("    // GETTER");


        return codeExample;
    }

    private JavaCodeExample createMyBeanCodeExample() {
        final JavaCodeExample myBean = new JavaCodeExample("MyBean.java", "mybean", "tree.demo", "MyBean", true);

        if (selectionAjaxType != SelectionAjaxType.NONE) {
            myBean.addImport("import org.butterfaces.event.TreeNodeSelectionEvent");
            myBean.addImport("import org.butterfaces.event.TreeNodeSelectionListener");
        }
        myBean.addImport("import org.butterfaces.model.tree.Node");
        myBean.addImport("import org.butterfaces.model.tree.DefaultNodeImpl");
        myBean.addImport("import javax.faces.view.ViewScoped");
        myBean.addImport("import javax.inject.Named");

        if (selectionAjaxType != SelectionAjaxType.NONE) {
            myBean.addInterfaces("TreeNodeSelectionListener");
        }

        if (isAjaxRendered()) {
            myBean.appendInnerContent("    private Node selectedNode;\n");
        }
        myBean.appendInnerContent("    private Node rootNode;\n");
        myBean.appendInnerContent("    public Node getTreeModel() {");
        myBean.appendInnerContent("        if (rootNode == null) {");

        if (selectedTreeTemplateType == TreeTemplateType.CUSTOM) {
            myBean.appendInnerContent("            final Node mailNode = new DefaultNodeImpl(\"Mail\", new NodeData());");
            myBean.appendInnerContent("            mailNode.getSubNodes().add(new DefaultNodeImpl<>(\"Inbox\", new NodeData()));");
            myBean.appendInnerContent("            mailNode.getSubNodes().add(new DefaultNodeImpl<>(\"Drafts\", new NodeData()));");
            myBean.appendInnerContent("            mailNode.getSubNodes().add(new DefaultNodeImpl<>(\"Sent\", new NodeData()));");
            myBean.appendInnerContent("            mailNode.getSubNodes().add(new DefaultNodeImpl<>(\"Tagged\", new NodeData()));");
            myBean.appendInnerContent("            mailNode.getSubNodes().add(new DefaultNodeImpl<>(\"Folders\", new NodeData()));");
            myBean.appendInnerContent("            mailNode.getSubNodes().add(new DefaultNodeImpl<>(\"Trash\", new NodeData()));");
            myBean.appendInnerContent("\n");
            myBean.appendInnerContent("            final Node<NodeData> rootNode = new DefaultNodeImpl<>(\"rootNode\", new NodeData());");
            myBean.appendInnerContent("            rootNode.getSubNodes().add(mailNode);");
            myBean.appendInnerContent("            rootNode.getSubNodes().add(new DefaultNodeImpl<>(\"Special Sign \\\"\\'\", new NodeData()));");
            myBean.appendInnerContent("            return rootNode;");
        } else {
            myBean.appendInnerContent("            final Node firstChild = new DefaultNodeImpl(\"firstChild\");");
            myBean.appendInnerContent("            firstChild.setDescription(\"23 unread\");");
            if (showcaseTreeNode.getSelectedIconType() == TreeIconType.GLYPHICON) {
                myBean.appendInnerContent("            firstChild.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
            } else if (showcaseTreeNode.getSelectedIconType() == TreeIconType.IMAGE) {
                myBean.appendInnerContent("            firstChild.setImageIcon(\"some/path/16.png\");");
            }
            myBean.appendInnerContent("            final Node secondChild = new DefaultNodeImpl(\"second\");");
            if (!allExpanded) {
                myBean.appendInnerContent("            secondChild.setCollapsed(true);");
            }
            if (showcaseTreeNode.getSelectedIconType() == TreeIconType.GLYPHICON) {
                myBean.appendInnerContent("            secondChild.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
            } else if (showcaseTreeNode.getSelectedIconType() == TreeIconType.IMAGE) {
                myBean.appendInnerContent("            secondChild.setImageIcon(\"some/path/16.png\");");
            }
            myBean.appendInnerContent("            secondChild.getSubNodes().add(new DefaultNodeImpl(\"...\"))");
            myBean.appendInnerContent("            ...");
            myBean.appendInnerContent("            rootNode = new DefaultNodeImpl(\"rootNode\");");
            if (showcaseTreeNode.getSelectedIconType() == TreeIconType.IMAGE) {
                myBean.appendInnerContent("            rootNode.setImageIcon(\"some/path/16.png\");");
            } else if (showcaseTreeNode.getSelectedIconType() == TreeIconType.GLYPHICON) {
                myBean.appendInnerContent("            rootNode.setGlyphiconIcon(\"glyphicon glyphicon-folder-open\");");
            }
        }
        myBean.appendInnerContent("            rootNode.getSubNodes().add(firstChild);");
        myBean.appendInnerContent("            rootNode.getSubNodes().add(secondChild);");
        myBean.appendInnerContent("        }");
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

    public List<EnumTreeBoxWrapper> getAjaxSelectionTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final SelectionAjaxType type : SelectionAjaxType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public List<EnumTreeBoxWrapper> getIconTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final TreeIconType type : TreeIconType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public List<EnumTreeBoxWrapper> getSeachBarModeTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final TreeSearchBarModeType type : TreeSearchBarModeType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
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
        if (StringUtils.isNotEmpty(spinnerText)) {
            xhtmlCodeExample.appendInnerContent("                spinnerText=\"" + spinnerText + "\"");
        }
        if (StringUtils.isNotEmpty(noEntriesText)) {
            xhtmlCodeExample.appendInnerContent("                noEntriesText=\"" + noEntriesText + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                rendered=\"" + this.isRendered() + "\">");

        if (selectedTreeTemplateType == TreeTemplateType.CUSTOM) {
            xhtmlCodeExample.appendInnerContent("            <!-- use attributes from node or node.data-->");
            xhtmlCodeExample.appendInnerContent("            <!-- javascript mustache syntax is used -->");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"template\">");
            xhtmlCodeExample.appendInnerContent("                <strong>{{title}}</strong>");
            xhtmlCodeExample.appendInnerContent("                <div>Created: {{createDate}}</div>");
            xhtmlCodeExample.appendInnerContent("                <div>UUID: {{uuid}}</div>");
            xhtmlCodeExample.appendInnerContent("            </facet>");
        }

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

    public Object getValues() {
        switch (selectedTreeTemplateType) {
            case CUSTOM:
                final DefaultNodeImpl<NodeData> mailNode = new DefaultNodeImpl<>("Mail", new NodeData());
                mailNode.getSubNodes().add(new DefaultNodeImpl<>("Inbox", new NodeData()));
                mailNode.getSubNodes().add(new DefaultNodeImpl<>("Drafts", new NodeData()));
                mailNode.getSubNodes().add(new DefaultNodeImpl<>("Sent", new NodeData()));
                mailNode.getSubNodes().add(new DefaultNodeImpl<>("Tagged", new NodeData()));
                mailNode.getSubNodes().add(new DefaultNodeImpl<>("Folders", new NodeData()));
                mailNode.getSubNodes().add(new DefaultNodeImpl<>("Trash", new NodeData()));

                final Node<NodeData> rootNode = new DefaultNodeImpl<>("rootNode", new NodeData());
                rootNode.getSubNodes().add(mailNode);
                rootNode.getSubNodes().add(new DefaultNodeImpl<>("Special Sign \"\'", new NodeData()));
                return rootNode;
            default:
                return showcaseTreeNode.getTree();
        }
    }

    public List<EnumTreeBoxWrapper> getTreeTemplateTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final TreeTemplateType type : TreeTemplateType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
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
        return this.selectedNode;
    }

    public boolean isAllExpanded() {
        return allExpanded;
    }

    public void setAllExpanded(boolean allExpanded) {
        this.allExpanded = allExpanded;

        toggleNodeExpansion(showcaseTreeNode.getTree(), allExpanded);
    }

    public void toggleNodeExpansion(final Node node, final boolean expanded) {
        node.setCollapsed(!expanded);

        for (Object subNode : node.getSubNodes()) {
            toggleNodeExpansion((Node) subNode, expanded);
        }
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

    public TreeTemplateType getSelectedTreeTemplateType() {
        return selectedTreeTemplateType;
    }

    public void setSelectedTreeTemplateType(TreeTemplateType selectedTreeTemplateType) {
        this.selectedTreeTemplateType = selectedTreeTemplateType;
    }

    public ShowcaseTreeNode getShowcaseTreeNode() {
        return showcaseTreeNode;
    }

    public Integer getToManyVisibleItemsRenderDelay() {
        return toManyVisibleItemsRenderDelay;
    }

    public void setToManyVisibleItemsRenderDelay(Integer toManyVisibleItemsRenderDelay) {
        this.toManyVisibleItemsRenderDelay = toManyVisibleItemsRenderDelay;
    }

    public Integer getToManyVisibleItemsThreshold() {
        return toManyVisibleItemsThreshold;
    }

    public void setToManyVisibleItemsThreshold(Integer toManyVisibleItemsThreshold) {
        this.toManyVisibleItemsThreshold = toManyVisibleItemsThreshold;
    }

    public String getNoEntriesText() {
        return noEntriesText;
    }

    public void setNoEntriesText(String noEntriesText) {
        this.noEntriesText = noEntriesText;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public void setSpinnerText(String spinnerText) {
        this.spinnerText = spinnerText;
    }
}
