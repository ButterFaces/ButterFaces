package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlCalendar;
import de.larmic.butterfaces.component.partrenderer.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;

@FacesRenderer(componentFamily = HtmlCalendar.COMPONENT_FAMILY, rendererType = HtmlCalendar.RENDERER_TYPE)
public class CalendarRenderer extends AbstractTextRenderer<HtmlCalendar> {

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlCalendar calendar = (HtmlCalendar) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!calendar.isReadonly()) {
            super.encodeSuperEnd(context, component);
            if (calendar.isPickDate() || calendar.isPickTime()) {
                writer.startElement("span", component);
                writer.writeAttribute("class", "input-group-addon", null);
                writer.startElement("span", component);
                // jquery plugin will add icon here
                writer.endElement("span");
                writer.endElement("span");
            }
        }

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(calendar, writer);

        // render tooltip elements if necessary
        new TooltipPartRenderer().renderTooltip(calendar, writer);

        if (calendar.isPickDate() || calendar.isPickTime()) {
            writer.startElement("script", calendar);
            writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".input-group", createJQueryPluginCall(calendar)), null);
            writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".input-group", createJQueryPluginCallback(calendar)), null);
            writer.endElement("script");
        }

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    private String createJQueryPluginCall(HtmlCalendar calendar) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        final String calendarDate = StringUtils.getNotNullValue(calendar.getGlyphiconDate(), "glyphicon glyphicon-calendar");
        final String calendarTime = StringUtils.getNotNullValue(calendar.getGlyphiconTime(), "glyphicon glyphicon-time");
        final String calendarUp = StringUtils.getNotNullValue(calendar.getGlyphiconUp(), "glyphicon glyphicon-chevron-up");
        final String calendarDown = StringUtils.getNotNullValue(calendar.getGlyphiconDown(), "glyphicon glyphicon-chevron-down");

        jQueryPluginCall.append("datetimepicker({");
        jQueryPluginCall.append("pickTime: " + calendar.isPickTime() + ",");
        jQueryPluginCall.append("pickDate: " + calendar.isPickDate() + ",");
        jQueryPluginCall.append("sideBySide: " + calendar.isSideBySide() + ",");
        jQueryPluginCall.append("icons: {");
        jQueryPluginCall.append("time: '" + calendarTime + "',");
        jQueryPluginCall.append("date: '" + calendarDate + "',");
        jQueryPluginCall.append("up: '" + calendarUp + "',");
        jQueryPluginCall.append("down: '" + calendarDown + "'");
        jQueryPluginCall.append("},");
        jQueryPluginCall.append("language: \"" + calendar.getLanguage() + "\"");
        jQueryPluginCall.append("})");
        return jQueryPluginCall.toString();
    }

    private String createJQueryPluginCallback(HtmlCalendar calendar) {
        final StringBuilder jQueryPluginCall = new StringBuilder();
        jQueryPluginCall.append("on(\"dp.change\", function (e) {");
        jQueryPluginCall.append(RenderUtils.createJQueryBySelector(calendar.getClientId(), ".butter-input-component") + "trigger('keyup');");
        jQueryPluginCall.append("})");
        return jQueryPluginCall.toString();
    }

}
