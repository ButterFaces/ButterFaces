package de.larmic.butterfaces.component.renderkit.html_basic.action;

import de.larmic.butterfaces.component.html.action.HtmlCommandLink;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.resolver.AjaxClientIdResolver;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
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

    private static final String WEB_XML_AJAX_PROCESSING_TEXT = "de.larmic.butterfaces.ajaxProcessingTextOnRequest";

    /**
     * Will be set in renderAsActive if f:ajax child with onevent attribute exists.
     */
    private String onEventCallback = null;

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        this.onEventCallback = null;

        super.encodeBegin(context, component);
    }

    @Override
    protected void writeCommonLinkAttributes(final ResponseWriter writer, final UIComponent component) throws IOException {
        final HtmlCommandLink link = (HtmlCommandLink) component;
        final String styleClass = (String) component.getAttributes().get("styleClass");

        final StringBuilder generatedStyleClass = new StringBuilder(StringUtils.isEmpty(styleClass) ? "" : styleClass);

        if (link.isDisabled()) {
            generatedStyleClass.append(" disabled");
        }

        if (generatedStyleClass.length() > 0) {
            writer.writeAttribute("class", generatedStyleClass.toString(), "styleClass");
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        final HtmlCommandLink link = (HtmlCommandLink) component;
        final ResponseWriter responseWriter = context.getResponseWriter();

        if (link.isAjaxDisableLinkOnRequest()) {
            responseWriter.startElement("script", component);
            responseWriter.writeText("function " + getOnEventListenerName(component) + "(data) {", null);
            if (StringUtils.isNotEmpty(onEventCallback)) {
                responseWriter.writeText("    " + onEventCallback + "(data);", null);
            }

            final String processingText = createAjaxProcessingText(link);

            final AjaxClientIdResolver ajaxClientIdResolver = new AjaxClientIdResolver(link);
            final String jQueryIDSelector = link.isAjaxDisableRenderRegionsOnRequest()
                    ? ajaxClientIdResolver.getjQueryRenderIDSelector() : "undefined";

            responseWriter.writeText("    disableOnClick(data, " +
                    link.isAjaxShowWaitingDotsOnRequest() + ",'" +
                    link.getValue() + "','" +
                    processingText + "'," +
                    link.isAjaxHideGlyphiconOnRequest() + ",'" +
                    jQueryIDSelector + "');", null);
            responseWriter.writeText("}", null);
            responseWriter.endElement("script");
        }
    }

    private String createAjaxProcessingText(final HtmlCommandLink link) {
        if (StringUtils.isNotEmpty(link.getAjaxProcessingTextOnRequest())) {
            return link.getAjaxProcessingTextOnRequest();
        }

        final String ajaxProcessingTextByWebXml = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(WEB_XML_AJAX_PROCESSING_TEXT);

        return StringUtils.isEmpty(ajaxProcessingTextByWebXml)  ? "Processing" : ajaxProcessingTextByWebXml;
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
        final String glyphicon = commandLink.getGlyphicon();

        if (glyphicon != null && !"".equals(glyphicon)) {
            writer.startElement("span", commandLink);
            writer.writeAttribute("class", "butter-component-glyphicon " + glyphicon, null);
            writer.endElement("span");
        }
    }
}
