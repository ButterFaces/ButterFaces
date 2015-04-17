package de.larmic.butterfaces.component.html;

import de.larmic.butterfaces.component.base.component.UIComponentBase;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

@ResourceDependencies({
		@ResourceDependency(library = "butterfaces-css", name = "butterfaces-default.css", target = "head"),
		@ResourceDependency(library = "butterfaces-css", name = "butterfaces-tooltip.css", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-fixed.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-tooltip.jquery.js", target = "head")
})
@FacesComponent(HtmlTooltip.COMPONENT_TYPE)
public class HtmlTooltip extends UIComponentBase {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.tooltip";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.TooltipRenderer";

	protected static final String PROPERTY_JQUERY_TARGET_SELECTOR = "jQueryTargetSelector";
	protected static final String PROPERTY_TITLE = "title";
	protected static final String PROPERTY_TRIGGER = "trigger";
	protected static final String PROPERTY_PLACEMENT = "placement";

	public HtmlTooltip() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	public String getjQueryTargetSelector() {
		return (String) this.getStateHelper().eval(PROPERTY_JQUERY_TARGET_SELECTOR);
	}

	public void setjQueryTargetSelector(String jQueryTargetSelector) {
		this.updateStateHelper(PROPERTY_JQUERY_TARGET_SELECTOR, jQueryTargetSelector);
	}

	public String getTrigger() {
		return (String) this.getStateHelper().eval(PROPERTY_TRIGGER);
	}

	public void setTrigger(String trigger) {
		this.updateStateHelper(PROPERTY_TRIGGER, trigger);
	}

	public String getTitle() {
		return (String) this.getStateHelper().eval(PROPERTY_TITLE);
	}

	public void setTitle(String title) {
		this.updateStateHelper(PROPERTY_TITLE, title);
	}

	public String getPlacement() {
		return (String) this.getStateHelper().eval(PROPERTY_PLACEMENT);
	}

	public void setPlacement(String placement) {
		this.updateStateHelper(PROPERTY_PLACEMENT, placement);
	}
}
