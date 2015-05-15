package de.larmic.butterfaces.component.partrenderer;

import de.larmic.butterfaces.component.html.HtmlTooltip;
import de.larmic.butterfaces.component.html.feature.Validation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Created by larmic on 30.04.15.
 */
public class TooltipPartRenderer {

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
                htmlTooltip.setParent(component);
                htmlTooltip.encodeAll(context);
            }
        }
    }

    private boolean shouldRenderTooltip(UIComponent component) {
        return component instanceof Validation && !((Validation) component).isValid();
    }

}
