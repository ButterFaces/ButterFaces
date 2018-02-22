package org.butterfaces.component.html.resourcelibraries;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-number.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-link.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-prettyprint.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-section.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-table.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-table-toolbar.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-tags.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-tree.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-overlay.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-tooltip.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-autocomplete.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-dropdownlist.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-markdown.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-treebox.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-01-baseClass.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-bootstrap-fixes.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-dots.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-expandable.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-tooltip.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-clientSideFilter.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-modal.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-prettyprint.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-scroll.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-autocomplete.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-highlight.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-numberSpinner.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-markdown.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-js", name = "butterfaces-table-toolbar.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-link.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-ajax.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-guid.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-tooltip.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-overlay.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-waitingpanel.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-treebox.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-link.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-radiobox.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-util-object.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-util-string.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-maxlength.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-multiline-placeholder.jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-trivial-components-wrapper.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-css", name = "dist-butterfaces-bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-bootstrap.js", target = "head")
})
@FacesComponent(HtmlActivateLibraries.COMPONENT_TYPE)
public class HtmlActivateLibraries extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.butterfaces.component.activateLibraries";
    public static final String COMPONENT_FAMILY = "org.butterfaces.component.family";
    public static final String RENDERER_TYPE = "org.butterfaces.renderkit.html_basic.ActivateLibrariesRenderer";

    public HtmlActivateLibraries() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}
