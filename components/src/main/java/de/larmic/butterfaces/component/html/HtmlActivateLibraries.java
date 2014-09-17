package de.larmic.butterfaces.component.html;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

/**
 * Created by larmic on 17.09.14.
 */
@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery-1.11.1.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap-3.2.0.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap-3.2.0.min.js", target = "head")
})
@FacesComponent(HtmlActivateLibraries.COMPONENT_TYPE)
public class HtmlActivateLibraries extends UIComponentBase {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.activateLibraries";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.ActivateLibrariesRenderer";

    public HtmlActivateLibraries() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}
