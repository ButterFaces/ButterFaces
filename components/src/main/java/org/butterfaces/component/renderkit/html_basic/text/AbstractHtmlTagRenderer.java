/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.renderkit.html_basic.text;

import org.butterfaces.component.base.renderer.HtmlBasicInputRenderer;
import org.butterfaces.component.html.HtmlCheckBox;
import org.butterfaces.component.html.HtmlInputComponent;
import org.butterfaces.component.html.HtmlTooltip;
import org.butterfaces.component.html.InputComponentFacet;
import org.butterfaces.component.html.text.part.HtmlAutoComplete;
import org.butterfaces.component.partrenderer.*;
import org.butterfaces.util.StringJoiner;
import org.butterfaces.component.partrenderer.*;
import org.butterfaces.util.StringJoiner;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlInputTextarea;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * @author Lars Michaelis
 */
public abstract class AbstractHtmlTagRenderer<T extends HtmlInputComponent> extends HtmlBasicInputRenderer {

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        this.encodeBegin(context, component, "");
    }

    public void encodeBegin(final FacesContext context, final UIComponent component, final String additionalStyleClass) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        super.encodeBegin(context, component);

        final T htmlComponent = (T) component;

        final ResponseWriter writer = context.getResponseWriter();

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentBegin(component, writer, additionalStyleClass);

        this.appendEncodeBegin(htmlComponent, writer);

        // Render label if components label attribute is set
        new LabelPartRenderer().renderLabel(component, writer, createInputClientId(context, component));

        this.encodeBeginInnerWrapper(component, writer);
        this.encodeReadonly(htmlComponent, writer);
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final T htmlComponent = (T) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!htmlComponent.isReadonly()) {
            this.encodeEndContent(context, component, htmlComponent, writer);
        }

        this.encodeInnerEnd(component, writer);

        this.encodeEndInnerWrapper(component, writer);

        renderTooltipIfNecessary(context, component);

        this.encodeEnd(htmlComponent, writer);

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    protected String getHtmlTagName() {
        return "input";
    }

    protected void renderTooltipIfNecessary(final FacesContext context, final UIComponent component) throws IOException {
        for (UIComponent uiComponent : component.getChildren()) {
            if (uiComponent instanceof HtmlTooltip) {
                uiComponent.encodeAll(context);
                if (uiComponent.isRendered()) {
                    break;
                }
            }
        }

        new TooltipValidationRenderer().renderTooltipIfNecessary(context, component);
    }

    protected void appendEncodeBegin(final T component, final ResponseWriter writer) throws IOException {
        // implement me if needed
    }

    protected void encodeEndContent(FacesContext context, UIComponent component, HtmlInputComponent htmlComponent, ResponseWriter writer) throws IOException {
        final UIComponent inputGroupAddonLeftFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_ADDON_LEFT);
        final UIComponent inputGroupAddonRightFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_ADDON_RIGHT);
        final UIComponent inputGroupBtnLeftFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_BTN_LEFT);
        final UIComponent inputGroupBtnRightFacet = component.getFacet(InnerComponentWrapperPartRenderer.INPUT_GROUP_BTN_RIGHT);

        if (htmlComponent.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_ADDON) && inputGroupAddonLeftFacet != null) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "input-group-prepend", null);
            writer.startElement("span", component);
            writer.writeAttribute("class", "input-group-text", null);
            inputGroupAddonLeftFacet.encodeAll(context);
            writer.endElement("span");
            writer.endElement("div");
        } else if (htmlComponent.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_BTN) && inputGroupBtnLeftFacet != null) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "input-group-prepend", null);
            inputGroupBtnLeftFacet.encodeAll(context);
            writer.endElement("div");
        }

        super.encodeEnd(context, component);
        this.postEncodeInput(context, component);

        if (htmlComponent.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_ADDON) && inputGroupAddonRightFacet != null) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "input-group-append", null);
            writer.startElement("span", component);
            writer.writeAttribute("class", "input-group-text", null);
            inputGroupAddonRightFacet.encodeAll(context);
            writer.endElement("span");
            writer.endElement("div");
        } else if (htmlComponent.getSupportedFacets().contains(InputComponentFacet.BOOTSTRAP_INPUT_GROUP_LEFT_BTN) && inputGroupBtnRightFacet != null) {
            writer.startElement("div", component);
            writer.writeAttribute("class", "input-group-append", null);
            inputGroupBtnRightFacet.encodeAll(context);
            writer.endElement("div");
        }
    }

    protected void postEncodeInput(final FacesContext context, final UIComponent component) throws IOException {

    }

    /**
     * @return true if normal readonly render option should be used. False is custom readonly option is created by renderer implementation.
     */
    protected boolean encodeReadonly() {
        return true;
    }

    protected void encodeReadonly(T htmlComponent, ResponseWriter writer) throws IOException {
        if (encodeReadonly()) {
            // Render readonly span if components readonly attribute is set
            new ReadonlyPartRenderer().renderReadonly(htmlComponent, writer);
        }
    }

    protected void encodeEndInnerWrapper(UIComponent component, ResponseWriter writer) throws IOException {
        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(component, writer);
    }

    protected void encodeBeginInnerWrapper(UIComponent component, ResponseWriter writer) throws IOException {
        // Open inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperBegin(component, writer);
    }

    protected void encodeInnerEnd(final UIComponent component, final ResponseWriter writer) throws IOException {
        // override me
    }

    protected void encodeEnd(final T component, final ResponseWriter writer) throws IOException {
        // override me
    }

    /**
     * Helper to call super end from sub classes without calling encodeEnd of this class. This is not nice but is works at this time.
     * TODO fix it.
     *
     * @param component the component
     * @param context   {@link FacesContext}
     * @throws IOException thrown by writer
     */
    protected void encodeSuperEnd(final FacesContext context, final UIComponent component) throws IOException {
        super.encodeEnd(context, component);
    }

    /**
     * Method copied from super class to add html features.
     */
    @Override
    protected void getEndTextToRender(final FacesContext context,
                                      final UIComponent component,
                                      final String currentValue) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();

        if (component instanceof UIInput) {
            writer.startElement(getHtmlTagName(), component);

            writer.writeAttribute("id", createInputClientId(context, component), "clientId");
            writer.writeAttribute("name", component.getClientId(context), "clientId");

            // TODO extract protected method
            // for text area based components the value can't be set this way
            if (!(component instanceof HtmlInputTextarea) && !(component instanceof HtmlCheckBox) && currentValue != null) {
                writer.writeAttribute("value", currentValue, "value");
            }

            // default
            this.renderBooleanValue(component, writer, "disabled");
            this.renderBooleanValue(component, writer, "ismap");
            this.renderBooleanValue(component, writer, "readonly");

            // html 5
            this.renderStringValue(component, writer, "autocomplete", "off");
            this.renderStringValue(component, writer, "placeholder");
            this.renderBooleanValue(component, writer, "autoFocus");
            this.renderBooleanValue(component, writer, "pattern");
            this.renderStringValue(component, writer, "min");
            this.renderStringValue(component, writer, "max");

            // html
            this.renderStringValue(component, writer, "alt");
            this.renderStringValue(component, writer, "dir");
            this.renderStringValue(component, writer, "lang");
            this.renderStringValue(component, writer, "maxlength");
            this.renderStringValue(component, writer, "role");
            this.renderStringValue(component, writer, "size");
            this.renderStringValue(component, writer, "style");
            this.renderStringValue(component, writer, "tabindex");
            this.renderStringValue(component, writer, "title");

            // events
            this.renderEventValue(component, writer, "onblur", "blur");
            this.renderEventValue(component, writer, "onclick", "click");
            this.renderEventValue(component, writer, "ondblclick", "dblclick");
            this.renderEventValue(component, writer, "onfocus", "focus");
            this.renderEventValue(component, writer, "onkeydown", "keydown");
            this.renderEventValue(component, writer, "onkeypress", "keypress");
            this.renderEventValue(component, writer, "onkeyup", "keyup");
            this.renderEventValue(component, writer, "onmousedown", "mousedown");
            this.renderEventValue(component, writer, "onmousemove", "mousemove");
            this.renderEventValue(component, writer, "onmouseout", "mouseout");
            this.renderEventValue(component, writer, "onmouseover", "mouseover");
            this.renderEventValue(component, writer, "onmouseup", "mouseup");
            this.renderEventValue(component, writer, "onselect", "select");
            this.renderEventValue(component, writer, "onchange", "change");

            this.renderInputStyleClass((HtmlInputComponent) component, writer);

            this.encodeTagType(component, writer);
            this.encodeAdditionalTagAttributes(component, writer, currentValue);

            this.renderAdditionalInputAttributes(context, component, writer);

            // for text area based components the value must be set this way
            if (component instanceof HtmlInputTextarea && currentValue != null) {
                writer.writeText(currentValue, null);
            }

            writer.endElement(getHtmlTagName());
        }
    }

    private String createInputClientId(FacesContext context, UIComponent component) {
        final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
        return component.getClientId(context) + separatorChar + "inner";
    }

    protected void encodeTagType(final UIComponent component, final ResponseWriter writer) throws IOException {
        this.renderStringValue(component, writer, "type");
    }

    protected void encodeAdditionalTagAttributes(final UIComponent component,
                                                 final ResponseWriter writer,
                                                 final String currentValue) throws IOException {
        // implement me if needed
    }

    /**
     * When using {@link AbstractHtmlTagRenderer} input component will be wrapped. Component style class will be set to
     * component wrapper. This method renders inner input component bootstrap classes and validation markers.
     *
     * @param component the component
     * @param writer    html response writer
     * @throws IOException thrown by writer
     */
    protected void renderInputStyleClass(final HtmlInputComponent component,
                                         final ResponseWriter writer) throws IOException {
        final String validationMarkerClass = !component.isValid() ? Constants.INVALID_STYLE_CLASS : null;
        final String styleClass = StringJoiner.on(" ")
                .join(Constants.INPUT_COMPONENT_MARKER)
                .join(Constants.BOOTSTRAP_FORM_CONTROL)
                .join(validationMarkerClass)
                .toString();
        writer.writeAttribute("class", styleClass, "styleClass");
    }

    protected void renderAdditionalInputAttributes(final FacesContext context,
                                                   final UIComponent component,
                                                   final ResponseWriter writer) throws IOException {
    }

    @Override
    public void encodeChildren(final FacesContext context,
                               final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                // ignore tooltips (will be rendered before)
                if (!(child instanceof HtmlTooltip) && !(child instanceof HtmlAutoComplete)) {
                    encodeRecursive(context, child);
                }
            }
        }
    }
}
