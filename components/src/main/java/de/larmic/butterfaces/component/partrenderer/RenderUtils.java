package de.larmic.butterfaces.component.partrenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class RenderUtils {

    /**
     * Renders a script element with a function call for a jquery plugin
     *
     * @param elementId          the html id of the element without leading # (e.g. 'myElementId')
     * @param pluginFunctionCall the plugin function call (e.g. 'tooltip()')
     * @param writer
     * @param uiComponent
     */
    public static final void renderJQueryPluginCall(final String elementId, final String pluginFunctionCall,
                                                    final ResponseWriter writer, final UIComponent uiComponent)
            throws IOException {
        final String jsCall = createJQueryPluginCall(elementId, pluginFunctionCall);

        writer.startElement("script", uiComponent);
        writer.writeText(jsCall, null);
        writer.endElement("script");
    }

    public static String createJQueryPluginCall(final String elementId, final String pluginFunctionCall) {
        final StringBuilder jsCall = new StringBuilder();

        jsCall.append("jQuery(function () {");
        jsCall.append("jQuery(");
        jsCall.append("document.getElementById('");
        jsCall.append(elementId);
        jsCall.append("')");
        jsCall.append(").");
        jsCall.append(pluginFunctionCall);
        jsCall.append(";");
        jsCall.append("});");

        return jsCall.toString();
    }

}
