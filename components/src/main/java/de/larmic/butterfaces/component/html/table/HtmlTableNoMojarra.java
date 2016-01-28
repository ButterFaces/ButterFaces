/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table;

import de.larmic.butterfaces.component.html.repeat.HtmlRepeat;

import javax.faces.component.FacesComponent;

/**
 * This class is experimental and still in progress
 *
 * @author Lars Michaelis
 */
@FacesComponent(HtmlTableNoMojarra.COMPONENT_TYPE)
public class HtmlTableNoMojarra extends HtmlRepeat {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.tableNoMojarra";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.renderkit.html_basic.TableRendererNoMojarra";

    public HtmlTableNoMojarra() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

}
