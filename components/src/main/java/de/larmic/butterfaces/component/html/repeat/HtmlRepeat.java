/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2015.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.repeat;

import javax.faces.component.FacesComponent;

/**
 * &lt;b:repeat&gt; is a non visible component used to iterate through a data model.
 *
 * @author Lars Michaelis
 */
@FacesComponent(HtmlRepeat.COMPONENT_TYPE)
public class HtmlRepeat extends UIDataAdaptor {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.repeat";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.RepeatRenderer";

    public HtmlRepeat() {
        setRendererType(RENDERER_TYPE);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}
