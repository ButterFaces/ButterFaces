package de.larmic.butterfaces.component.renderkit.html_basic;

import com.sun.faces.renderkit.html_basic.CommandLinkRenderer;
import de.larmic.butterfaces.component.html.HtmlGlyphiconCommandLink;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
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
@FacesRenderer(componentFamily = HtmlGlyphiconCommandLink.COMPONENT_FAMILY, rendererType = HtmlGlyphiconCommandLink.RENDERER_TYPE)
public class GlyphiconCommandLinkRenderer extends CommandLinkRenderer {

    /**
     * Will be set in renderAsActive if f:ajax child with onevent attribute exists.
     */
    private String onEventCallback = null;

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        final ResponseWriter responseWriter = context.getResponseWriter();

        responseWriter.startElement("script", component);
        responseWriter.writeText("function glyphiconLinkListener(data) {", null);
        if (StringUtils.isNotEmpty(onEventCallback)) {
            responseWriter.writeText("    " + onEventCallback + "(data);", null);
        }
        responseWriter.writeText("    disableOnClick(data);", null);
        responseWriter.writeText("}", null);
        responseWriter.endElement("script");
    }

    @Override
    protected void writeValue(final UIComponent component, final ResponseWriter writer) throws IOException {
        final HtmlGlyphiconCommandLink commandLink = (HtmlGlyphiconCommandLink) component;

        this.writeGlyphiconIfNecessary(commandLink, writer);

        super.writeValue(component, writer);

        this.writeWaitingDotsIfNecessary(commandLink, writer);
    }

    @Override
    protected void renderAsActive(FacesContext context, UIComponent component) throws IOException {
        AjaxBehavior ajaxBehavior = null;

        if (((HtmlGlyphiconCommandLink) component).isDisableOnClick()) {
            final Map behaviors = (AbstractMap) ((ClientBehaviorHolder) component).getClientBehaviors();

            final List<AjaxBehavior> actionBehaviours = (List<AjaxBehavior>) behaviors.get("action");

            if (actionBehaviours != null && !actionBehaviours.isEmpty()) {
                for (AjaxBehavior actionBehaviour : actionBehaviours) {
                    ajaxBehavior = actionBehaviour;
                    if (StringUtils.isNotEmpty(ajaxBehavior.getOnevent())) {
                        onEventCallback = ajaxBehavior.getOnevent();
                    }

                    ajaxBehavior.setOnevent("glyphiconLinkListener");
                }
            } else {
                ajaxBehavior = new AjaxBehavior();
                ajaxBehavior.setOnevent("listener");
                ((ClientBehaviorHolder) component).addClientBehavior("action", ajaxBehavior);
            }
        }

        super.renderAsActive(context, component);

        // reset ajax behaviour because otherwise a render of this component will not be work correctly (wrong js
        // callback is registered if onevent is set on f:ajax.
        if (ajaxBehavior != null) {
            ajaxBehavior.setOnevent(onEventCallback);
        }
    }

    protected void writeWaitingDotsIfNecessary(final HtmlGlyphiconCommandLink commandLink,
                                               final ResponseWriter writer) throws IOException {
        if (commandLink.isDisableOnClick()) {
            writer.startElement("span", commandLink);
            writer.writeAttribute("class", "butter-component-glyphicon-processing", null);
            writer.endElement("span");
        }
    }

    protected void writeGlyphiconIfNecessary(final HtmlGlyphiconCommandLink commandLink,
                                             final ResponseWriter writer) throws IOException {
        final String glyphicon = commandLink.getGlyphicon();

        if (glyphicon != null && !"".equals(glyphicon)) {
            writer.startElement("span", commandLink);
            writer.writeAttribute("class", "butter-component-glyphicon " + glyphicon, null);
            writer.endElement("span");
        }
    }
}
