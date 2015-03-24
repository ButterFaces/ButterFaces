package de.larmic.butterfaces.component.renderkit.html_basic;

import de.larmic.butterfaces.component.html.HtmlModalPanel;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

/**
 * Created by larmic on 31.07.14.
 */
@FacesRenderer(componentFamily = HtmlModalPanel.COMPONENT_FAMILY, rendererType = HtmlModalPanel.RENDERER_TYPE)
public class ModalPanelRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final ResponseWriter writer = context.getResponseWriter();
        final HtmlModalPanel modalPanel = (HtmlModalPanel) component;

        writer.startElement(ELEMENT_DIV, component);
        writeIdAttribute(context, writer, component);
        if (StringUtils.isNotEmpty(modalPanel.getStyleClass())) {
            writer.writeAttribute(ATTRIBUTE_CLASS, "modal fade " + modalPanel.getStyleClass(), null);
        } else {
            writer.writeAttribute(ATTRIBUTE_CLASS, "modal fade", null);
        }
        if (StringUtils.isNotEmpty(modalPanel.getStyle())) {
            writer.writeAttribute(ATTRIBUTE_STYLE, modalPanel.getStyle(), null);
        }
        writer.writeAttribute("tabindex", "-1", null);
        writer.writeAttribute("role", "dialog", null);
        writer.writeAttribute("aria-hidden", "true", null);
        writer.writeAttribute("data-modal-id", component.getId(), null);

        writer.startElement(ELEMENT_DIV, component);
        writer.writeAttribute(ATTRIBUTE_CLASS, "modal-dialog", null);

        writer.startElement(ELEMENT_DIV, component);
        writer.writeAttribute(ATTRIBUTE_CLASS, "modal-content", null);

        this.writerHeader(modalPanel, writer);

        writer.startElement(ELEMENT_DIV, component);
        writer.writeAttribute(ATTRIBUTE_CLASS, "modal-body", null);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlModalPanel modalPanel = (HtmlModalPanel) component;
        final ResponseWriter writer = context.getResponseWriter();

        writer.endElement(ELEMENT_DIV); // modal-body

        this.writeFooter(modalPanel, context);

        writer.endElement(ELEMENT_DIV); // modal-content
        writer.endElement(ELEMENT_DIV); // modal-dialog
        writer.endElement(ELEMENT_DIV); // modal fade
    }

    private void writerHeader(final HtmlModalPanel component, final ResponseWriter writer) throws IOException {
        if (StringUtils.isNotEmpty(component.getTitle())) {
            writer.startElement(ELEMENT_DIV, component);
            writer.writeAttribute(ATTRIBUTE_CLASS, "modal-header", null);
            writer.startElement("h4", component);
            writer.writeAttribute(ATTRIBUTE_CLASS, "modal-title", null);
            writer.writeText(component.getTitle(), component, null);
            writer.endElement("h4");
            writer.endElement(ELEMENT_DIV);
        }
    }

    private void writeFooter(final HtmlModalPanel component, final FacesContext context) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        writer.startElement(ELEMENT_DIV, component);
        writer.writeAttribute(ATTRIBUTE_CLASS, "modal-footer", null);

        writer.startElement(ELEMENT_SPAN, component);
        writer.writeAttribute(ATTRIBUTE_CLASS, "btn btn-danger pull-left", null);
        writer.writeAttribute("onClick", "butter.modal.close('" + component.getId() + "');", null);
        if (StringUtils.isNotEmpty(component.getCancelButtonText())) {
            writer.writeText(component.getCancelButtonText(), component, null);
        } else {
            writer.writeText("Close", component, null);
        }
        writer.endElement(ELEMENT_SPAN);

        final UIComponent additionalFooter = this.getFacet(component, "additional-footer");

        if (additionalFooter != null) {
            writer.startElement(ELEMENT_SPAN, component);
            writer.writeAttribute(ATTRIBUTE_CLASS, "pull-right", null);
            additionalFooter.encodeAll(context);
            writer.endElement(ELEMENT_SPAN);
        }

        writer.endElement(ELEMENT_DIV);
    }

}
