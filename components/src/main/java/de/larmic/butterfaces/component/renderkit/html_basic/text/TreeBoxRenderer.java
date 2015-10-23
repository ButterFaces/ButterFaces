package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlTreeBox;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlTreeBox.COMPONENT_FAMILY, rendererType = HtmlTreeBox.RENDERER_TYPE)
public class TreeBoxRenderer extends AbstractTextRenderer<HtmlTreeBox> {

    @Override
    protected boolean encodeReadonly() {
        return false;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-tags");
    }

    @Override
    protected void encodeInnerEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlTreeBox treeBox = (HtmlTreeBox) component;

        if (treeBox.isReadonly()) {
            writer.startElement(ELEMENT_DIV, component);
            writer.writeAttribute("class", "butter-component-value", null);
            super.encodeSuperEnd(FacesContext.getCurrentInstance(), component);
            writer.endElement(ELEMENT_DIV);
        }
    }

    @Override
    protected void encodeEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlTreeBox treeBox = (HtmlTreeBox) component;

        writer.startElement("script", component);
        writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".butter-input-component", createJQueryPluginCallTivial(treeBox)), null);
        writer.endElement("script");
    }

    private String createJQueryPluginCallTivial(final HtmlTreeBox tags) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        jQueryPluginCall.append("TrivialComboBox({");
        jQueryPluginCall.append("\n    allowFreeText: true,");
        //jQueryPluginCall.append("\n    emptyEntry: {");
        //jQueryPluginCall.append("\n    \"displayValue\": \"Please select...\",");
        //jQueryPluginCall.append("\n    \"imageUrl\": \"-\",");
        //jQueryPluginCall.append("\n    \"additionalInfo\": \"\"");
        //jQueryPluginCall.append("\n    },");
        jQueryPluginCall.append("\n    templates: ['" + TreeRenderer.DEFAULT_TEMPLATE + "']");
        jQueryPluginCall.append("});");

        return jQueryPluginCall.toString();
    }
}
