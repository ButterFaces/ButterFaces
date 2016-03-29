/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlRadioBox2;
import de.larmic.butterfaces.component.renderkit.html_basic.text.AbstractHtmlTagRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlRadioBox2.COMPONENT_FAMILY, rendererType = HtmlRadioBox2.RENDERER_TYPE)
public class RadioBox2Renderer extends AbstractHtmlTagRenderer<HtmlRadioBox2> {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        encodeBegin(context, component, "butter-component-radiobox");
    }

    @Override
    protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        if (component instanceof HtmlRadioBox2) {
            final HtmlRadioBox2 radioBox = (HtmlRadioBox2) component;
            final String radioBoxClientId = radioBox.getClientId();
            final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());

            if (radioBox.getValues() instanceof Iterable) {
                int iterator = 0;
                for (java.lang.Object o : (Iterable) radioBox.getValues()) {
                    renderRadioBoxItem(writer, radioBox, radioBoxClientId, separatorChar, iterator, o);
                    iterator++;
                }
            }
        }
    }

    @Override
    protected void setSubmittedValue(UIComponent component, Object value) {
        super.setSubmittedValue(component, value != null ? value : "");
    }

    private void renderRadioBoxItem(ResponseWriter writer,
                                    HtmlRadioBox2 radioBox,
                                    String radioBoxClientId,
                                    char separatorChar,
                                    int itemCounter,
                                    Object listItem) throws IOException {
        final String radioItemClientId = radioBoxClientId + separatorChar + itemCounter;
        writer.startElement("div", radioBox);
        writer.writeAttribute("class", "radio", "class");
        writer.startElement("input", radioBox);
        writer.writeAttribute("id", radioItemClientId, "clientId");
        writer.writeAttribute("type", "radio", "input");
        writer.writeAttribute("type", "radio", "input");
        writer.writeAttribute("name", radioBoxClientId, "name");
        writer.endElement("input");
        writer.startElement("label", radioBox);
        writer.writeAttribute("for", radioItemClientId, "for");
        writer.writeText(convertItemToText(listItem), radioBox, null);
        writer.endElement("label");
        writer.endElement("div");
    }

    private String convertItemToText(Object listItem) {
        if (listItem instanceof SelectItem) {
            return ((SelectItem) listItem).getLabel();
        }

        return listItem.toString();
    }
}
