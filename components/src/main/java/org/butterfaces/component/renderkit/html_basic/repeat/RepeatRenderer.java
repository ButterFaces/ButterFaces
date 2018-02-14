/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.renderkit.html_basic.repeat;

import org.butterfaces.component.html.repeat.HtmlRepeat;
import org.butterfaces.component.html.repeat.visitor.DataVisitResult;
import org.butterfaces.component.html.repeat.visitor.DataVisitor;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlRepeat.COMPONENT_FAMILY, rendererType = HtmlRepeat.RENDERER_TYPE)
public class RepeatRenderer extends Renderer {

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        final HtmlRepeat repeat = (HtmlRepeat) component;

        try {
            DataVisitor visitor = new DataVisitor() {
                public DataVisitResult process(FacesContext context, Integer rowKey) throws IOException {
                    repeat.setRowKey(context, rowKey);

                    if (repeat.isRowAvailable() && repeat.getChildCount() > 0) {
                        for (UIComponent child : repeat.getChildren()) {
                            child.encodeAll(context);
                        }
                    }

                    return DataVisitResult.CONTINUE;
                }
            };

            repeat.walk(context, visitor);
        } finally {
            repeat.setRowKey(context, null);
        }
    }
}
