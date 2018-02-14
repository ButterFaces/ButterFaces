package org.butterfaces.component.renderkit.html_basic.text;

import java.io.IOException;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.butterfaces.component.html.text.HtmlCalendar;
import org.butterfaces.component.partrenderer.InnerComponentWrapperPartRenderer;
import org.butterfaces.component.partrenderer.OuterComponentWrapperPartRenderer;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.partrenderer.InnerComponentWrapperPartRenderer;
import org.butterfaces.component.partrenderer.OuterComponentWrapperPartRenderer;
import org.butterfaces.component.partrenderer.RenderUtils;
import org.butterfaces.util.StringUtils;

@FacesRenderer(componentFamily = HtmlCalendar.COMPONENT_FAMILY, rendererType = HtmlCalendar.RENDERER_TYPE)
public class CalendarRenderer extends AbstractHtmlTagRenderer<HtmlCalendar> {

    private static final Logger LOG = Logger.getLogger(CalendarRenderer.class.getName());

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final HtmlCalendar calendar = (HtmlCalendar) component;
        final ResponseWriter writer = context.getResponseWriter();

        if (!calendar.isReadonly()) {
            super.encodeSuperEnd(context, component);
            if (calendar.isPickDate() || calendar.isPickTime()) {
                writer.startElement("div", component);
                writer.writeAttribute("class", "input-group-append cursor-pointer", null);
                writer.writeAttribute("data-toggle", "datetimepicker", null);
                writer.startElement("span", component);
                if (!calendar.isPickDate()) {
                    writer.writeAttribute("class", "input-group-text glyphicon glyphicon-time", null);
                } else {
                    writer.writeAttribute("class", "input-group-text glyphicon glyphicon-calendar", null);
                }
                writer.endElement("span");
                writer.endElement("div");
            }
        }

        // Close inner component wrapper div
        new InnerComponentWrapperPartRenderer().renderInnerWrapperEnd(calendar, writer);

        // render tooltip elements if necessary
        renderTooltipIfNecessary(context, calendar);

        if (!calendar.isReadonly() && (calendar.isPickDate() || calendar.isPickTime())) {
            writer.startElement("script", calendar);

            writer.writeText(
                RenderUtils.createJQueryPluginCall(component.getClientId(), null, createJQueryPluginCall(calendar), "var elementId = ButterFaces.Guid.newGuid();"), null);
            writer.endElement("script");
        }

        // Open outer component wrapper div
        new OuterComponentWrapperPartRenderer().renderComponentEnd(writer);
    }

    String createJQueryPluginCall(HtmlCalendar calendar) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        jQueryPluginCall.append("find('.input-group')");
        jQueryPluginCall.append(".attr('id', elementId)");
        jQueryPluginCall.append(".attr('data-target-input', 'nearest')");
        jQueryPluginCall.append(".find('input')");
        jQueryPluginCall.append(".attr('data-target', '#' + elementId)");
        jQueryPluginCall.append(".addClass('datetimepicker-input')");
        jQueryPluginCall.append(".siblings('.input-group-append')");
        jQueryPluginCall.append(".attr('data-target', '#' + elementId)");
        jQueryPluginCall.append(".parent()");
        jQueryPluginCall.append(".datetimepicker({");

        if (StringUtils.isNotEmpty(calendar.getFormat())) {
            jQueryPluginCall.append("format: '").append(calendar.getFormat()).append("',");
        } else {
            if (calendar.isPickDate() && !calendar.isPickTime()) {
                jQueryPluginCall.append("format: 'L',");
            } else if (!calendar.isPickDate() && calendar.isPickTime()) {
                jQueryPluginCall.append("format: 'LT',");
            }
        }

        if (StringUtils.isNotEmpty(calendar.getLocale())) {
            jQueryPluginCall.append("locale: '" + calendar.getLocale() + "',");
        }
        if (calendar.getViewMode() != null) {
            jQueryPluginCall.append("viewMode: '" + calendar.getViewMode().getValue() + "',");
        }
        if (calendar.isSideBySide()) {
            jQueryPluginCall.append("sideBySide: true,");
        }

        final String calendarDate = StringUtils.getNotNullValue(calendar.getIconDate(), "glyphicon glyphicon-calendar");
        final String calendarTime = StringUtils.getNotNullValue(calendar.getIconTime(), "glyphicon glyphicon-time");
        final String calendarUp = StringUtils.getNotNullValue(calendar.getIconUp(), "glyphicon glyphicon-chevron-up");
        final String calendarDown = StringUtils.getNotNullValue(calendar.getIconDown(), "glyphicon glyphicon-chevron-down");
        jQueryPluginCall.append("icons: {");
        jQueryPluginCall.append("time: '" + calendarTime + "',");
        jQueryPluginCall.append("date: '" + calendarDate + "',");
        jQueryPluginCall.append("up: '" + calendarUp + "',");
        jQueryPluginCall.append("down: '" + calendarDown + "'");
        jQueryPluginCall.append("}");

        jQueryPluginCall.append("})");

        return jQueryPluginCall.toString();
    }
}
