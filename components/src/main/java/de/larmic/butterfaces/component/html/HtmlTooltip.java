package de.larmic.butterfaces.component.html;

import de.larmic.butterfaces.component.base.component.UIComponentBase;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

@ResourceDependencies({
		@ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
		@ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-tooltip.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
       @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.js", target = "head"),
		@ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-tooltip.js", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-tooltip.jquery.js", target = "head")
})
@FacesComponent(HtmlTooltip.COMPONENT_TYPE)
public class HtmlTooltip extends UIComponentBase {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.tooltip";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.TooltipRenderer";

	protected static final String PROPERTY_FOR = "for";
	protected static final String PROPERTY_TITLE = "title";
	protected static final String PROPERTY_TRIGGER = "trigger";
	protected static final String PROPERTY_PLACEMENT = "placement";
	protected static final String PROPERTY_VIEWPORT = "viewport";
	protected static final String PROPERTY_PLACEMENT_FUNCTION = "placementFunction";
	protected static final String PROPERTY_POPOVER_min_WIDTH = "minVerticalOffset";
	protected static final String PROPERTY_POPOVER_min_HEIGHT = "minHorizontalOffset";
	protected static final String PROPERTY_ON_SHOW = "onShow";
	protected static final String PROPERTY_ON_SHOWN = "onShown";
	protected static final String PROPERTY_ON_HIDE = "onHide";
	protected static final String PROPERTY_ON_HIDDEN = "onHidden";

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

	public String getFor() {
		return (String) this.getStateHelper().eval(PROPERTY_FOR);
	}

	public void setFor(String jQueryTargetSelector) {
		this.updateStateHelper(PROPERTY_FOR, jQueryTargetSelector);
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

	public String getViewport() {
		return (String) this.getStateHelper().eval(PROPERTY_VIEWPORT);
	}

	public void setViewport(String viewport) {
		this.updateStateHelper(PROPERTY_VIEWPORT, viewport);
	}

	public String getPlacementFunction() {
		return (String) this.getStateHelper().eval(PROPERTY_PLACEMENT_FUNCTION);
	}

	public void setPlacementFunction(String placementFunction) {
		this.updateStateHelper(PROPERTY_PLACEMENT_FUNCTION, placementFunction);
	}

	public Integer getMinVerticalOffset() {
		return (Integer) this.getStateHelper().eval(PROPERTY_POPOVER_min_WIDTH);
	}

	public void setMinVerticalOffset(Integer minVerticalOffset) {
		this.updateStateHelper(PROPERTY_POPOVER_min_WIDTH, minVerticalOffset);
	}

	public Integer getMinHorizontalOffset() {
		return (Integer) this.getStateHelper().eval(PROPERTY_POPOVER_min_HEIGHT);
	}

	public void setMinHorizontalOffset(Integer minHorizontalOffset) {
		this.updateStateHelper(PROPERTY_POPOVER_min_HEIGHT, minHorizontalOffset);
	}

    public String getOnShow() {
        return (String) this.getStateHelper().eval(PROPERTY_ON_SHOW);
    }

    public void setOnShow(String onShow) {
        this.updateStateHelper(PROPERTY_ON_SHOW, onShow);
    }

    public String getOnShown() {
        return (String) this.getStateHelper().eval(PROPERTY_ON_SHOWN);
    }

    public void setOnShown(String onShown) {
        this.updateStateHelper(PROPERTY_ON_SHOWN, onShown);
    }

    public String getOnHide() {
        return (String) this.getStateHelper().eval(PROPERTY_ON_HIDE);
    }

    public void setOnHide(String onHide) {
        this.updateStateHelper(PROPERTY_ON_HIDE, onHide);
    }

    public String getOnHidden() {
        return (String) this.getStateHelper().eval(PROPERTY_ON_HIDDEN);
    }

    public void setOnHidden(String onHidden) {
        this.updateStateHelper(PROPERTY_ON_HIDDEN, onHidden);
    }
}
