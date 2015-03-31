package de.larmic.butterfaces.component.html.resourcelibraries;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

/**
 * Created by larmic on 17.09.14.
 */
@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-configurable", name = "jquery.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "bootstrap.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "prettify.css", target = "head"),
        @ResourceDependency(library = "butterfaces-configurable", name = "prettify.js", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-link.css", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-prettyprint.css", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-section.css", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-table.css", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-tags.css", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-tree.css", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-waiting.css", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-clientSideFilter.css", target = "head"),
        @ResourceDependency(library = "butterfaces-css", name = "butterfaces-filterable.css", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-bootstrap-fixes.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-fixed.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-overlay.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-disableElements.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-dots.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-expandable.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-link.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-maxlength.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-table.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-tagsinput.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-ajax.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-tooltip.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-tree.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-waitingpanel.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-clientSideFilter.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-filterableSelect.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-modal.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-number.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-prettyprint.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-scroll.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "01-moment-with-locales.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-datetimepicker.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-datetimepicker.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-tagsinput.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-tagsinput.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "jquery.bootstrap-touchspin.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "jquery.bootstrap-touchspin.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "jquery.inputmask.min.js", target = "head"),
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
