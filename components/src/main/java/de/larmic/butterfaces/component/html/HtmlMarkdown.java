package de.larmic.butterfaces.component.html;

import de.larmic.butterfaces.component.html.feature.*;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.html.HtmlInputTextarea;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-markdown.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-util-string.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-maxlength.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-multiline-placeholder.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-01-baseClass.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-markdown.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "markdown.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "to-markdown.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-markdown-with-languages.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-markdown.min.css", target = "head")
})
@FacesComponent(HtmlMarkdown.COMPONENT_TYPE)
public class HtmlMarkdown extends HtmlInputTextarea implements HtmlInputComponent, Placeholder, AutoFocus, Tooltip, Label, Readonly, MaxLength {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.markdown";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.MarkdownRenderer";

    protected static final String PROPERTY_HIDE_LABEL = "hideLabel";
    protected static final String PROPERTY_MAXLENGTH = "maxLength";
    protected static final String PROPERTY_PLACEHOLDER = "placeholder";
    protected static final String PROPERTY_HTML5_AUTO_FOCUS = "autoFocus";
    protected static final String PROPERTY_LANGUAGE = "language";

    public HtmlMarkdown() {
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

    @Override
    public boolean isAutoFocus() {
        final Object eval = this.getStateHelper().eval(PROPERTY_HTML5_AUTO_FOCUS);
        return eval == null ? false : (Boolean) eval;
    }

    @Override
    public void setAutoFocus(final boolean autoFocus) {
        this.updateStateHelper(PROPERTY_HTML5_AUTO_FOCUS, autoFocus);
    }

    @Override
    public boolean isHideLabel() {
        final Object eval = this.getStateHelper().eval(PROPERTY_HIDE_LABEL);
        return eval == null ? false : (Boolean) eval;
    }

    public void setHideLabel(final boolean hideLabel) {
        this.updateStateHelper(PROPERTY_HIDE_LABEL, hideLabel);
    }

    public String getPlaceholder() {
        return (String) this.getStateHelper().eval(PROPERTY_PLACEHOLDER);
    }

    public void setPlaceholder(final String placeholder) {
        this.updateStateHelper(PROPERTY_PLACEHOLDER, placeholder);
    }

    public Integer getMaxLength() {
        return (Integer) this.getStateHelper().eval(PROPERTY_MAXLENGTH);
    }

    public void setMaxLength(final Integer maxLength) {
        this.updateStateHelper(PROPERTY_MAXLENGTH, maxLength);
    }

    public String getLanguage() {
        final Object eval = this.getStateHelper().eval(PROPERTY_LANGUAGE);
        return eval == null ? "en" : (String) eval;
    }

    public void setLanguage(String language) {
        this.updateStateHelper(PROPERTY_LANGUAGE, language);;
    }

    private void updateStateHelper(final String propertyName, final Object value) {
        this.getStateHelper().put(propertyName, value);

        final ValueExpression ve = this.getValueExpression(propertyName);

        if (ve != null) {
            ve.setValue(this.getFacesContext().getELContext(), value);
        }
    }
}
