package de.larmic.butterfaces.component.html.text;

import de.larmic.butterfaces.component.html.InputComponentFacet;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-tags.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-trivial-components.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "mustache.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "jquery.position.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-trivial-components-wrapper.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-tags.jquery.js", target = "head")
})
@FacesComponent(HtmlTags.COMPONENT_TYPE)
public class HtmlTags extends HtmlText {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.tags";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.TagsRenderer";


    protected static final String PROPERTY_ENTRIES = "entries";
    protected static final String PROPERTY_MAX_TAGS = "maxTags";
    protected static final String PROPERTY_DISTINCT = "distinct";
    protected static final String PROPERTY_CONFIRM_KEYS = "confirmKeys";
    protected static final String PROPERTY_AUTOCOMPLETE = "autoComplete";

    public HtmlTags() {
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

    public Integer getMaxTags() {
        return (Integer) this.getStateHelper().eval(PROPERTY_MAX_TAGS);
    }

    public void setMaxTags(Integer maxTags) {
        this.updateStateHelper(PROPERTY_MAX_TAGS, maxTags);
    }

    public List<Object> getEntries() {
        return (List<Object>) this.getStateHelper().eval(PROPERTY_ENTRIES);
    }

    public void setEntries(List<Object> entries) {
        this.updateStateHelper(PROPERTY_ENTRIES, entries);
    }

    public boolean isDistinct() {
        final Object eval = this.getStateHelper().eval(PROPERTY_DISTINCT);
        return eval == null ? true : (Boolean) eval;
    }

    public void setDistinct(boolean distinct) {
        this.updateStateHelper(PROPERTY_DISTINCT, distinct);
    }

    public String getConfirmKeys() {
        return (String) this.getStateHelper().eval(PROPERTY_CONFIRM_KEYS);
    }

    public void setConfirmKeys(String confirmKeys) {
        this.updateStateHelper(PROPERTY_CONFIRM_KEYS, confirmKeys);
    }

    public boolean isAutoComplete() {
        final Object eval = this.getStateHelper().eval(PROPERTY_AUTOCOMPLETE);
        return eval == null ? false : (Boolean) eval;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.updateStateHelper(PROPERTY_AUTOCOMPLETE, autoComplete);
    }
}
