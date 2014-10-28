package de.larmic.butterfaces.component.html.tree;

import de.larmic.butterfaces.model.tree.Node;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({
        @ResourceDependency(library = "css", name = "butterfaces-tree.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery-2.1.1.min.js", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-tree.jquery.js", target = "head")
})
@FacesComponent(HtmlTree.COMPONENT_TYPE)
public class HtmlTree extends UIComponentBase {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.tree";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.TreeRenderer";

    protected static final String PROPERTY_VALUE = "value";
    protected static final String PROPERTY_EXPANSION_CLASS = "expansionClass";
    protected static final String PROPERTY_COLLAPSING_CLASS = "collapsingClass";

    public HtmlTree() {
        super();
        this.setRendererType(RENDERER_TYPE);
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

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
