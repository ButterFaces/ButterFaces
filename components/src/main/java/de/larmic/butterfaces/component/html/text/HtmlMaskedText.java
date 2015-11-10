package de.larmic.butterfaces.component.html.text;

import de.larmic.butterfaces.component.html.feature.Masked;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-fixed.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-util.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "jquery.inputmask.bundle.js", target = "head")
})
@FacesComponent(HtmlMaskedText.COMPONENT_TYPE)
public class HtmlMaskedText extends HtmlText implements Masked {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.maskedText";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.MaskedInputRenderer";

    protected static final String PROPERTY_INPUT_MASK = "inputMask";

    public HtmlMaskedText() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getInputMask() {
        return (String) this.getStateHelper().eval(PROPERTY_INPUT_MASK);
    }

    @Override
    public void setInputMask(String inputMask) {
        this.updateStateHelper(PROPERTY_INPUT_MASK, inputMask);
    }
}
