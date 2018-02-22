package org.butterfaces.component.html.text;

import org.butterfaces.component.html.feature.Masked;
import org.butterfaces.component.html.feature.Masked;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "dist-butterfaces-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-bootstrap.js", target = "head")
})
@FacesComponent(HtmlMaskedText.COMPONENT_TYPE)
public class HtmlMaskedText extends HtmlText implements Masked {

    public static final String COMPONENT_TYPE = "org.butterfaces.component.maskedText";
    public static final String COMPONENT_FAMILY = "org.butterfaces.component.family";
    public static final String RENDERER_TYPE = "org.butterfaces.component.renderkit.html_basic.MaskedInputRenderer";

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
