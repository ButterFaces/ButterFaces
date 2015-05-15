package de.larmic.butterfaces.component.html.text;

import de.larmic.butterfaces.component.html.InputComponentFacet;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
		@ResourceDependency(library = "butterfaces-css", name = "butterfaces-default.css", target = "head"),
		@ResourceDependency(library = "butterfaces-js", name = "butterfaces-fixed.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-tagsinput.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-tagsinput.js", target = "head"),
		@ResourceDependency(library = "butterfaces-css", name = "butterfaces-tags.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-tagsinput.jquery.js", target = "head")
})
@FacesComponent(HtmlTags.COMPONENT_TYPE)
public class HtmlTags extends HtmlText {

	public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.tags";
	public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
	public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.TagsRenderer";


	protected static final String PROPERTY_MAX_TAGS = "maxTags";
	protected static final String PROPERTY_MAX_CHARS = "maxChars";
	protected static final String PROPERTY_TRIM_VALUE = "trimValue";
	protected static final String PROPERTY_ALLOW_DUPLICATES = "allowDuplicates";
	protected static final String PROPERTY_CONFIRM_KEYS = "confirmKeys";

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

	public Integer getMaxChars() {
		return (Integer) this.getStateHelper().eval(PROPERTY_MAX_CHARS);
	}

	public void setMaxChars(Integer maxChars) {
		this.updateStateHelper(PROPERTY_MAX_CHARS, maxChars);
	}

	public boolean isTrimValue() {
		final Object eval = this.getStateHelper().eval(PROPERTY_TRIM_VALUE);
		return eval == null ? false : (Boolean) eval;
	}

	public void setTrimValue(boolean trimValue) {
		this.updateStateHelper(PROPERTY_TRIM_VALUE, trimValue);
	}

	public boolean isAllowDuplicates() {
		final Object eval = this.getStateHelper().eval(PROPERTY_ALLOW_DUPLICATES);
		return eval == null ? false : (Boolean) eval;
	}

	public void setAllowDuplicates(boolean allowDuplicates) {
		this.updateStateHelper(PROPERTY_ALLOW_DUPLICATES, allowDuplicates);
	}

	public String getConfirmKeys() {
		return (String) this.getStateHelper().eval(PROPERTY_CONFIRM_KEYS);
	}

	public void setConfirmKeys(String confirmKeys) {
		this.updateStateHelper(PROPERTY_CONFIRM_KEYS, confirmKeys);
	}
}
