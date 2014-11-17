package de.larmic.butterfaces.component.html;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlCommandLink;

/**
 * Created by larmic on 16.09.14.
 */
@ResourceDependencies({
        @ResourceDependency(library = "css", name = "butterfaces.css", target = "head"),
        @ResourceDependency(library = "css", name = "butterfaces-link.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery-2.1.1.min.js", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-dots.jquery.js", target = "head"),
        @ResourceDependency(library = "js", name = "butterfaces-link.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap-3.3.0.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap-3.3.0.min.js", target = "head")})
@FacesComponent(HtmlGlyphiconCommandLink.COMPONENT_TYPE)
public class HtmlGlyphiconCommandLink extends HtmlCommandLink {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.glyphiconCommandLink";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.GlyphiconCommandLinkRenderer";

    protected static final String PROPERTY_GLYPHICON = "glyphicon";
    protected static final String PROPERTY_AJAX_DISABLE_LINK_ON_REQUEST = "ajaxDisableLinkOnRequest";
    protected static final String PROPERTY_AJAX_SHOW_WAITING_DOTS_ON_REQUEST = "ajaxShowWaitingDotsOnRequest";
    protected static final String PROPERTY_AJAX_PROCESSING_TEXT_ON_REQUEST = "ajaxProcessingTextOnRequest";
    protected static final String PROPERTY_AJAX_HIDE_GLYPHICON_ON_REQUEST = "ajaxHideGlyphiconOnRequest";
    protected static final String PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST = "ajaxDisableRenderRegionsOnRequest";

    public HtmlGlyphiconCommandLink() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getStyleClass() {
        final String styleClass = super.getStyleClass();

        if (styleClass == null) {
            return "butter-component-link";
        }

        return styleClass + " butter-component-link";
    }

    public String getGlyphicon() {
        return (String) this.getStateHelper().eval(PROPERTY_GLYPHICON);
    }

    public void setGlyphicon(final String glyphicon) {
        this.updateStateHelper(PROPERTY_GLYPHICON, glyphicon);
    }

    public boolean isAjaxDisableLinkOnRequest() {
        final Object eval = this.getStateHelper().eval(PROPERTY_AJAX_DISABLE_LINK_ON_REQUEST);
        return eval == null ? true : (Boolean) eval;
    }

    public void setAjaxDisableLinkOnRequest(boolean ajaxDisableLinkOnRequest) {
        this.updateStateHelper(PROPERTY_AJAX_DISABLE_LINK_ON_REQUEST, ajaxDisableLinkOnRequest);
    }

    public boolean isAjaxShowWaitingDotsOnRequest() {
        final Object eval = this.getStateHelper().eval(PROPERTY_AJAX_SHOW_WAITING_DOTS_ON_REQUEST);
        return eval == null ? true : (Boolean) eval;
    }

    public void setAjaxShowWaitingDotsOnRequest(boolean ajaxShowWaitingDotsOnRequest) {
        this.updateStateHelper(PROPERTY_AJAX_SHOW_WAITING_DOTS_ON_REQUEST, ajaxShowWaitingDotsOnRequest);
    }

    public String getAjaxProcessingTextOnRequest() {
        return (String) this.getStateHelper().eval(PROPERTY_AJAX_PROCESSING_TEXT_ON_REQUEST);
    }

    public void setAjaxProcessingTextOnRequest(String ajaxProcessingTextOnRequest) {
        this.updateStateHelper(PROPERTY_AJAX_PROCESSING_TEXT_ON_REQUEST, ajaxProcessingTextOnRequest);
    }

    public boolean isAjaxHideGlyphiconOnRequest() {
        final Object eval = this.getStateHelper().eval(PROPERTY_AJAX_HIDE_GLYPHICON_ON_REQUEST);
        return eval == null ? true : (Boolean) eval;
    }

    public void setAjaxHideGlyphiconOnRequest(boolean ajaxHideGlyphiconOnRequest) {
        this.updateStateHelper(PROPERTY_AJAX_HIDE_GLYPHICON_ON_REQUEST, ajaxHideGlyphiconOnRequest);
    }

    public boolean isAjaxDisableRenderRegionsOnRequest() {
        final Object eval = this.getStateHelper().eval(PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST);
        return eval == null ? true : (Boolean) eval;
    }

    public void setAjaxDisableRenderRegionsOnRequest(boolean ajaxDisableRenderRegionsOnRequest) {
        this.updateStateHelper(PROPERTY_AJAX_DISABLE_RENDER_REGION_ON_REQUEST, ajaxDisableRenderRegionsOnRequest);
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
