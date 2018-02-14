package org.butterfaces.component.partrenderer;

import java.io.IOException;
import java.util.Map;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.butterfaces.util.StringUtils;
import org.butterfaces.util.StringUtils;

public class RenderUtils {

    /**
     * Renders a script element with a function call for a jquery plugin
     *
     * @param elementId          the html id of the element without leading # (e.g. 'myElementId')
     * @param pluginFunctionCall the plugin function call (e.g. 'tooltip()')
     * @param writer             component writer
     * @param uiComponent        component to add script
     *
     * @throws java.io.IOException if writer throws an error
     */
    public static void renderJQueryPluginCall(final String elementId, final String pluginFunctionCall,
                                              final ResponseWriter writer, final UIComponent uiComponent)
        throws IOException {
        final String jsCall = createJQueryPluginCall(elementId, pluginFunctionCall);

        writer.startElement("script", uiComponent);
        writer.writeText(jsCall, null);
        writer.endElement("script");
    }

    public static String createJQueryPluginCall(final String elementId, final String pluginFunctionCall) {
        return createJQueryPluginCall(elementId, null, pluginFunctionCall, null);
    }

    public static String createJQueryPluginCall(final String elementId, final String childSelector, final String pluginFunctionCall) {
        return createJQueryPluginCall(elementId, childSelector, pluginFunctionCall, null);
    }

    public static String createJQueryPluginCall(final String elementId, final String childSelector,
                                                final String pluginFunctionCall, String previousCalls) {
        final StringBuilder jsCall = new StringBuilder();

        jsCall.append("jQuery(function () {");
        if (StringUtils.isNotEmpty(previousCalls)) {
            jsCall.append(previousCalls);
        }
        jsCall.append(createJQueryBySelector(elementId, childSelector));
        jsCall.append(".");
        jsCall.append(pluginFunctionCall);
        jsCall.append(";");
        jsCall.append("});");

        return jsCall.toString();
    }

    public static String createJQueryBySelector(String elementId, String childSelector) {
        final StringBuilder jsCall = new StringBuilder();

        jsCall.append("jQuery(");
        jsCall.append("document.getElementById('");
        jsCall.append(elementId);
        jsCall.append("')");
        jsCall.append(")");

        if (StringUtils.isNotEmpty(childSelector)) {
            jsCall.append(".");
            jsCall.append("find('");
            jsCall.append(childSelector);
            jsCall.append("')");
        }

        return jsCall.toString();
    }

    public static String createOptionsStringForJQueryPluginCall(Map<String, String> options) {
        StringBuilder sb = new StringBuilder("{");
        int index = 0;
        for (String key : options.keySet()) {
            if (index > 0) {
                sb.append(", ");
            }
            sb.append(key).append(": '").append(options.get(key)).append("'");
            index++;
        }
        sb.append("}");
        return sb.toString();
    }
}
