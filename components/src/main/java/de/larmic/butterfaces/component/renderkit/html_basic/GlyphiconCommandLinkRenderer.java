package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.renderkit.html_basic.CommandLinkRenderer;
import de.larmic.butterfaces.component.html.HtmlGlyphiconCommandLink;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 16.09.14.
 */
@FacesRenderer(componentFamily = HtmlGlyphiconCommandLink.COMPONENT_FAMILY, rendererType = HtmlGlyphiconCommandLink.RENDERER_TYPE)
public class GlyphiconCommandLinkRenderer extends CommandLinkRenderer {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        // TODO write jquery plugin
    }

    @Override
    protected void writeValue(final UIComponent component, final ResponseWriter writer) throws IOException {
        writeGlyphiconIfNecessary(component, writer);

        super.writeValue(component, writer);
    }

    protected void writeGlyphiconIfNecessary(final UIComponent component,
                                             final ResponseWriter writer) throws IOException {
        final HtmlGlyphiconCommandLink commandLink = (HtmlGlyphiconCommandLink) component;
        final String glyphicon = commandLink.getGlyphicon();

        if (glyphicon != null && !"".equals(glyphicon)) {
            writer.startElement("span", component);
            writer.writeAttribute("class", "butter-component-glyphicon " + glyphicon, null);
            writer.endElement("span");
        }
    }
}
