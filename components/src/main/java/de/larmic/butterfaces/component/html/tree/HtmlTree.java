package de.larmic.butterfaces.component.html.tree;

import de.larmic.butterfaces.event.TreeNodeSelectionListener;
import de.larmic.butterfaces.model.tree.Node;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import java.util.Arrays;
import java.util.Collection;

@ResourceDependencies({
        @ResourceDependency(library = "css", name = "butterfaces-tree.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery-2.1.1.min.js", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-tree.jquery.js", target = "head")
})
@FacesComponent(HtmlTree.COMPONENT_TYPE)
public class HtmlTree extends UIComponentBase implements ClientBehaviorHolder {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.tree";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.TreeRenderer";

    protected static final String PROPERTY_VALUE = "value";
    protected static final String PROPERTY_EXPANSION_CLASS = "expansionClass";
    protected static final String PROPERTY_COLLAPSING_CLASS = "collapsingClass";
    protected static final String PROPERTY_HIDE_ROOT_NODE = "hideRootNode";
    protected static final String PROPERTY_NODE_SELECTION_LISTENER = "nodeSelectionListener";

    public HtmlTree() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public Collection<String> getEventNames() {
        return Arrays.asList("click");
    }

    @Override
    public String getDefaultEventName() {
        return "click";
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Node getValue() {
        return (Node) this.getStateHelper().eval(PROPERTY_VALUE);
    }

    public void setValue(final Node value) {
        this.updateStateHelper(PROPERTY_VALUE, value);
    }

    public TreeNodeSelectionListener getNodeSelectionListener() {
        return (TreeNodeSelectionListener) this.getStateHelper().eval(PROPERTY_NODE_SELECTION_LISTENER);
    }

    public void setNodeSelectionListener(final TreeNodeSelectionListener nodeSelectionListener) {
        this.updateStateHelper(PROPERTY_NODE_SELECTION_LISTENER, nodeSelectionListener);
    }

    public String getExpansionClass() {
        return (String) this.getStateHelper().eval(PROPERTY_EXPANSION_CLASS);
    }

    public void setExpansionClass(final String expansionClass) {
        this.updateStateHelper(PROPERTY_EXPANSION_CLASS, expansionClass);
    }

    public String getCollapsingClass() {
        return (String) this.getStateHelper().eval(PROPERTY_COLLAPSING_CLASS);
    }

    public void setCollapsingClass(final String collapsingClass) {
        this.updateStateHelper(PROPERTY_COLLAPSING_CLASS, collapsingClass);
    }

    public boolean isHideRootNode() {
        return Boolean.valueOf(getStateHelper().eval(PROPERTY_HIDE_ROOT_NODE, Boolean.TRUE).toString());
    }

    public void setHideRootNode(final boolean hideRootNode) {
        getStateHelper().put(PROPERTY_HIDE_ROOT_NODE, hideRootNode);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
