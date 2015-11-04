package de.larmic.butterfaces.component.html.text;

import de.larmic.butterfaces.component.html.InputComponentFacet;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
		@ResourceDependency(library = "butterfaces-css", name = "butterfaces-default.css", target = "head"),
		@ResourceDependency(library = "butterfaces-css", name = "butterfaces-treebox.css", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-fixed.js", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-util.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "mustache.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "jquery.position.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components.min.js", target = "head")
})
@FacesComponent(HtmlTreeBox.COMPONENT_TYPE)
public class HtmlTreeBox extends HtmlText {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.treeBox";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.TreeBoxRenderer";

    protected static final String PROPERTY_HIDE_ROOT_NODE = "hideRootNode";
	protected static final String PROPERTY_VALUES = "values";

	public HtmlTreeBox() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}

	@Override
	public List<InputComponentFacet> getSupportedFacets() {
		return Collections.emptyList();
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public Node getValues() {
		return (Node) this.getStateHelper().eval(PROPERTY_VALUES);
	}

	public void setValues(Node values) {
		this.updateStateHelper(PROPERTY_VALUES, values);
	}

    public boolean isHideRootNode() {
        return Boolean.valueOf(getStateHelper().eval(PROPERTY_HIDE_ROOT_NODE, Boolean.TRUE).toString());
    }

    public void setHideRootNode(final boolean hideRootNode) {
        getStateHelper().put(PROPERTY_HIDE_ROOT_NODE, hideRootNode);
    }
}
