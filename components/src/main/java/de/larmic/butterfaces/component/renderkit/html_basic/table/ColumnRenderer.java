package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.html.table.HtmlColumn;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 10.09.14.
 */
@FacesRenderer(componentFamily = HtmlColumn.COMPONENT_FAMILY, rendererType = HtmlColumn.RENDERER_TYPE)
public class ColumnRenderer extends com.sun.faces.renderkit.html_basic.HtmlBasicRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    }
}
