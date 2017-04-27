package de.larmic.butterfaces.component.html.text;

import de.larmic.butterfaces.component.html.InputComponentFacet;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import java.util.Arrays;
import java.util.List;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-treebox.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "mustache.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "jquery.position.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "trivial-components.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-treebox.js", target = "head"),
})
@FacesComponent(HtmlTreeBox.COMPONENT_TYPE)
public class HtmlTreeBox extends HtmlText {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.treeBox";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.TreeBoxRenderer";

    protected static final String PROPERTY_VALUES = "values";
    protected static final String PROPERTY_SPINNER_TEXT = "spinnerText";
    protected static final String PROPERTY_NO_ENTRIES_TEXT = "noEntriesText";
    protected static final String PROPERTY_SHOW_CLEAR_BUTTON = "showClearButton";
    protected static final String PROPERTY_INPUT_TEXT_PROPERTY = "inputTextProperty";

    public HtmlTreeBox() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public List<InputComponentFacet> getSupportedFacets() {
        return Arrays.asList(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_ADDON, InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_BTN);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Object getValues() {
        return this.getStateHelper().eval(PROPERTY_VALUES);
    }

    public void setValues(Object values) {
        this.updateStateHelper(PROPERTY_VALUES, values);
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

    public String getInputTextProperty() {
        return (String) getStateHelper().eval(PROPERTY_INPUT_TEXT_PROPERTY);
    }

    public void setInputTextProperty(String inputTextProperty) {
        getStateHelper().put(PROPERTY_INPUT_TEXT_PROPERTY, inputTextProperty);
    }

    public Boolean getShowClearButton() {
        return (Boolean) getStateHelper().eval(PROPERTY_SHOW_CLEAR_BUTTON);
    }

    public void setShowClearButton(Boolean showClearButton) {
        getStateHelper().put(PROPERTY_SHOW_CLEAR_BUTTON, showClearButton);
    }
}
