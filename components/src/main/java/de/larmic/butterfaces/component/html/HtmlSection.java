package de.larmic.butterfaces.component.html;

import de.larmic.butterfaces.component.base.component.UIComponentBase;
import de.larmic.butterfaces.component.html.feature.Label;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-section.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head") })
@FacesComponent(HtmlSection.COMPONENT_TYPE)
public class HtmlSection extends UIComponentBase implements Label {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.section";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.SectionRenderer";

    protected static final String PROPERTY_LABEL = "label";
    protected static final String PROPERTY_BADGE_TEXT = "badgeText";
    protected static final String PROPERTY_STYLE_CLASS = "styleClass";
    protected static final String PROPERTY_STYLE = "style";

	public HtmlSection() {
		super();
		this.setRendererType(RENDERER_TYPE);
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

    public String getLabel() {
        return (String) this.getStateHelper().eval(PROPERTY_LABEL);
    }

    public void setLabel(final String title) {
        this.updateStateHelper(PROPERTY_LABEL, title);
    }

    public String getStyleClass() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE_CLASS);
    }

    public void setStyleClass(final String styleClass) {
        this.updateStateHelper(PROPERTY_STYLE_CLASS, styleClass);
    }

    public String getStyle() {
        return (String) this.getStateHelper().eval(PROPERTY_STYLE);
    }

    public void setStyle(final String style) {
        this.updateStateHelper(PROPERTY_STYLE, style);
    }

    public String getBadgeText() {
        return (String) this.getStateHelper().eval(PROPERTY_BADGE_TEXT);
    }

    public void setBadgeText(final String badgeText) {
        this.updateStateHelper(PROPERTY_BADGE_TEXT, badgeText);
    }
}
