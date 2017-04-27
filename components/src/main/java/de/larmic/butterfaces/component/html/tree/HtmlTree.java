package de.larmic.butterfaces.component.html.tree;

import de.larmic.butterfaces.event.TreeNodeExpansionListener;
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
        @ResourceDependency(library = "butterfaces-external", name = "mustache.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-tree.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "jquery.position.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components.css", target = "head"),
      @ResourceDependency(library = "butterfaces-external", name = "trivial-components-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components.js", target = "head")
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
    protected static final String SEARCH_BAR_MODE = "searchBarMode";
    protected static final String PROPERTY_HTML5_PLACEHOLDER = "placeholder";
    protected static final String PROPERTY_NODE_SELECTION_LISTENER = "nodeSelectionListener";
    protected static final String PROPERTY_NODE_EXPANSION_LISTENER = "nodeExpansionListener";
    protected static final String PROPERTY_TO_MANY_VISIBLE_ITEMS_RENDER_DELAY = "toManyVisibleItemsRenderDelay";
    protected static final String PROPERTY_TO_MANY_VISIBLE_ITEMS_THRESHOLD = "toManyVisibleItemsThreshold";
    protected static final String PROPERTY_SPINNER_TEXT = "spinnerText";
    protected static final String PROPERTY_NO_ENTRIES_TEXT = "noEntriesText";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";

    public HtmlTree() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public Collection<String> getEventNames() {
        return Arrays.asList("click", "toggle");
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

    public TreeNodeExpansionListener getNodeExpansionListener() {
        return (TreeNodeExpansionListener) this.getStateHelper().eval(PROPERTY_NODE_EXPANSION_LISTENER);
    }

    public void setNodeExpansionListener(final TreeNodeExpansionListener nodeExpansionListener) {
        this.updateStateHelper(PROPERTY_NODE_EXPANSION_LISTENER, nodeExpansionListener);
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

    public String getStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE_CLASS);
    }

    public void setStyleClass(String styleClass) {
        this.updateStateHelper(PROPERTY_STYLE_CLASS, styleClass);
    }

    public String getStyle() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE);
    }

    public void setStyle(String style) {
        this.updateStateHelper(PROPERTY_STYLE, style);
    }

    public boolean isHideRootNode() {
        return Boolean.valueOf(getStateHelper().eval(PROPERTY_HIDE_ROOT_NODE, Boolean.TRUE).toString());
    }

    public void setHideRootNode(final boolean hideRootNode) {
        getStateHelper().put(PROPERTY_HIDE_ROOT_NODE, hideRootNode);
    }

    public String getPlaceholder() {
        return (String) this.getStateHelper().eval(PROPERTY_HTML5_PLACEHOLDER);
    }

    public void setPlaceholder(final String placeholder) {
        this.updateStateHelper(PROPERTY_HTML5_PLACEHOLDER, placeholder);
    }

    public String getSearchBarMode() {
        return (String) getStateHelper().eval(SEARCH_BAR_MODE);
    }

    public void setSearchBarMode(final String searchBarMode) {
        getStateHelper().put(SEARCH_BAR_MODE, searchBarMode);
    }

    public Integer getToManyVisibleItemsRenderDelay() {
        return (Integer) getStateHelper().eval(PROPERTY_TO_MANY_VISIBLE_ITEMS_RENDER_DELAY);
    }

    public void setToManyVisibleItemsRenderDelay(Integer toManyVisibleItemsRenderDelay) {
        getStateHelper().put(PROPERTY_TO_MANY_VISIBLE_ITEMS_RENDER_DELAY, toManyVisibleItemsRenderDelay);
    }

    public Integer getToManyVisibleItemsThreshold() {
        return (Integer) getStateHelper().eval(PROPERTY_TO_MANY_VISIBLE_ITEMS_THRESHOLD);
    }

    public void setToManyVisibleItemsThreshold(Integer toManyVisibleItemsThreshold) {
        getStateHelper().put(PROPERTY_TO_MANY_VISIBLE_ITEMS_THRESHOLD, toManyVisibleItemsThreshold);
    }

    public String getSpinnerText() {
        return (String) getStateHelper().eval(PROPERTY_SPINNER_TEXT);
    }

    public void setSpinnerText(String spinnerText) {
        getStateHelper().put(PROPERTY_SPINNER_TEXT, spinnerText);
    }

    public String getNoEntriesText() {
        return (String) getStateHelper().eval(PROPERTY_NO_ENTRIES_TEXT);
    }

    public void setNoEntriesText(String noEntriesText) {
        getStateHelper().put(PROPERTY_NO_ENTRIES_TEXT, noEntriesText);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
