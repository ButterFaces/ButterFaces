/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.partrenderer;

import org.butterfaces.component.html.HtmlTooltip;
import org.butterfaces.component.html.feature.Validation;
import org.butterfaces.component.html.HtmlTooltip;
import org.butterfaces.component.html.feature.Validation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * If validation error occurs existing tooltip will be set to rendered. If no tooltip child exists a new
 * {@link HtmlTooltip} will be added as child.
 *
 * @author Lars Michaelis
 */
public class TooltipValidationRenderer {

    public void renderTooltipIfNecessary(final FacesContext context, final UIComponent component) throws IOException {
        if (shouldRenderTooltip(component)) {
            boolean tooltipRendered = false;
            for (UIComponent uiComponent : component.getChildren()) {
                if (uiComponent instanceof HtmlTooltip) {
                    if (uiComponent.isRendered()) {
                        tooltipRendered = true;
                        break;
                    }
                }
            }

            if (!tooltipRendered) {
                final HtmlTooltip htmlTooltip = new HtmlTooltip();
                htmlTooltip.setPlacement(((Validation) component).getValidationErrorPlacement());
                htmlTooltip.setParent(component);
                htmlTooltip.encodeAll(context);
            }
        }
    }

    private boolean shouldRenderTooltip(UIComponent component) {
        return component instanceof Validation && !((Validation) component).isValid();
    }

}
