package de.larmic.butterfaces.component.renderkit.html_basic.action;

import de.larmic.butterfaces.component.html.action.HtmlCommandLink;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.resolver.AjaxClientIdResolver;
import de.larmic.butterfaces.resolver.WebXmlParameters;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

/**
 * Created by larmic on 16.09.14.
 */
@FacesRenderer(componentFamily = HtmlCommandLink.COMPONENT_FAMILY, rendererType = HtmlCommandLink.RENDERER_TYPE)
public class CommandLinkRenderer extends com.sun.faces.renderkit.html_basic.CommandLinkRenderer {

    private WebXmlParameters webXmlParameters;

    /**
     * Will be set in renderAsActive if f:ajax child with onevent attribute exists.
     */
    private String onEventCallback = null;

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        this.onEventCallback = null;

        final HtmlCommandLink link = (HtmlCommandLink) component;
        final ResponseWriter writer = context.getResponseWriter();

        webXmlParameters = new WebXmlParameters(context.getExternalContext());

        if (!link.isDisabled()) {
            super.encodeBegin(context, component);
        } else {
            writer.startElement("span", component);
            writeIdAttributeIfNecessary(context, writer, component);
            writeCommonLinkAttributes(writer, component);
            if (StringUtils.isNotEmpty(link.getStyle())) {
                writer.writeAttribute("style", link.getStyle(), "style");
            }
            this.writeValue(component, writer);
        }
    }

    @Override
    protected boolean shouldWriteIdAttribute(UIComponent component) {
        return true;
    }

    @Override
    protected void writeCommonLinkAttributes(final ResponseWriter writer, final UIComponent component) throws IOException {
        final HtmlCommandLink link = (HtmlCommandLink) component;
        final String styleClass = (String) component.getAttributes().get("styleClass");

        final StringBuilder generatedStyleClass = new StringBuilder(StringUtils.isEmpty(styleClass) ? "" : styleClass);

        if (link.isDisabled()) {
            generatedStyleClass.append(" btn-disabled");
        }
        if (StringUtils.isEmpty(link.getGlyphicon())) {
            generatedStyleClass.append(" no-glyphicon");
        }

        if (generatedStyleClass.length() > 0) {
            writer.writeAttribute("class", generatedStyleClass.toString(), "styleClass");
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String resetValues = params.get("javax.faces.partial.resetValues");
        final String render = params.get("javax.faces.partial.render");

        if (StringUtils.isNotEmpty(resetValues) && StringUtils.isNotEmpty(render) && Boolean.valueOf(resetValues)) {
            final String[] split = render.split(" ");

            for (String clientId : split) {
                final UIComponent renderComponent = context.getViewRoot().findComponent(clientId);
                resetValues(renderComponent);
            }
        }

        super.decode(context, component);
    }

    private void resetValues(final UIComponent component) {
        if (component == null) {
            return;
        }

        for (UIComponent child : component.getChildren()) {
            resetValues(child);
        }

        if (component instanceof UIOutput) {
            ((UIOutput) component).resetValue();
        } else if (component instanceof EditableValueHolder) {
            ((EditableValueHolder) component).resetValue();
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final HtmlCommandLink link = (HtmlCommandLink) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!link.isDisabled()) {
            if (link.isAjaxDisableLinkOnRequest()) {
                writer.startElement("script", component);
                writer.writeText("function " + getOnEventListenerName(component) + "(data) {", null);
                if (StringUtils.isNotEmpty(onEventCallback)) {
                    writer.writeText("    " + onEventCallback + "(data);", null);
                }

                final String processingText = createAjaxProcessingText(link);
                final String processingGlyphicon = createAjaxProcessingGlypicon(link);

                final AjaxClientIdResolver ajaxClientIdResolver = new AjaxClientIdResolver(link);
                final String jQueryIDSelector = link.isAjaxDisableRenderRegionsOnRequest()
                        ? ajaxClientIdResolver.getjQueryRenderIDSelector() : "undefined";

                writer.writeText("    butter.link.disableOnClick(data, " +
                        link.isAjaxShowWaitingDotsOnRequest() + ",'" +
                        link.getValue() + "','" +
                        processingText + "','" +
                        link.getGlyphicon() + "','" +
                        processingGlyphicon + "'," +
                        link.isAjaxHideGlyphiconOnRequest() + ",'" +
                        jQueryIDSelector + "');", null);
                writer.writeText("}", null);
                writer.endElement("script");
            }

            super.encodeEnd(context, component);
        } else {
            writer.endElement("span");
        }
    }

    private String createAjaxProcessingText(final HtmlCommandLink link) {
        if (StringUtils.isNotEmpty(link.getAjaxProcessingTextOnRequest())) {
            return link.getAjaxProcessingTextOnRequest();
        }

        return webXmlParameters.getAjaxProcessingTextOnRequest();
    }

    private String createAjaxProcessingGlypicon(final HtmlCommandLink link) {
        if (StringUtils.isNotEmpty(link.getAjaxProcessingGlyphiconOnRequest())) {
            return link.getAjaxProcessingGlyphiconOnRequest();
        }

        return webXmlParameters.getAjaxProcessingGlyphiconOnRequest();
    }

    @Override
    protected void writeValue(final UIComponent component, final ResponseWriter writer) throws IOException {
        final HtmlCommandLink commandLink = (HtmlCommandLink) component;

        this.writeGlyphiconIfNecessary(commandLink, writer);

        writer.startElement("span", component);
        writer.writeAttribute("class", "butter-component-glyphicon-text", null);
        super.writeValue(component, writer);
        writer.endElement("span");

        this.writeWaitingDotsIfNecessary(commandLink, writer);
    }

    @Override
    protected void renderAsActive(FacesContext context, UIComponent component) throws IOException {
        AjaxBehavior ajaxBehavior = null;

        if (((HtmlCommandLink) component).isAjaxDisableLinkOnRequest()) {
            final Map behaviors = (AbstractMap) ((ClientBehaviorHolder) component).getClientBehaviors();

            final List<AjaxBehavior> actionBehaviours = (List<AjaxBehavior>) behaviors.get("action");

            if (actionBehaviours != null && !actionBehaviours.isEmpty()) {
                for (AjaxBehavior actionBehaviour : actionBehaviours) {
                    ajaxBehavior = actionBehaviour;
                    if (StringUtils.isNotEmpty(ajaxBehavior.getOnevent())) {
                        onEventCallback = ajaxBehavior.getOnevent();
                    }

                    ajaxBehavior.setOnevent(getOnEventListenerName(component));
                }
            }
        }

        super.renderAsActive(context, component);

        // reset ajax behaviour because otherwise a render of this component will not be work correctly (wrong js
        // callback is registered if onevent is set on f:ajax.
        if (ajaxBehavior != null) {
            ajaxBehavior.setOnevent(onEventCallback);
        }
    }

    private String getOnEventListenerName(final UIComponent component) {
        final char separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
        return "glyphiconLinkListener" + "_" + component.getClientId().replace(separatorChar + "", "_");
    }

    protected void writeWaitingDotsIfNecessary(final HtmlCommandLink commandLink,
                                               final ResponseWriter writer) throws IOException {
        if (commandLink.isAjaxDisableLinkOnRequest()) {
            writer.startElement("span", commandLink);
            writer.writeAttribute("class", "butter-component-glyphicon-processing", null);
            writer.endElement("span");
        }
    }

    protected void writeGlyphiconIfNecessary(final HtmlCommandLink commandLink,
                                             final ResponseWriter writer) throws IOException {
        final String glyphicon = StringUtils.getNotNullValue(commandLink.getGlyphicon(), "");

        writer.startElement("span", commandLink);
        writer.writeAttribute("class", "butter-component-glyphicon " + glyphicon, null);
        writer.endElement("span");
    }
}
