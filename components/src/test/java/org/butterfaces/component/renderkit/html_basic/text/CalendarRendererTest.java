package org.butterfaces.component.renderkit.html_basic.text;

import org.butterfaces.component.html.text.HtmlCalendar;
import org.butterfaces.component.html.text.HtmlCalendarViewMode;
import org.butterfaces.component.html.text.HtmlCalendarViewMode;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CalendarRendererTest {

    private static final String PREPEND =
        "find('.input-group').attr('id', elementId).attr('data-target-input', 'nearest').find('input').attr('data-target', '#' + elementId).addClass('datetimepicker-input').siblings('.input-group-append').attr('data-target', '#' + elementId).parent().";

    @Test
    public void createJQueryPluginCall() {
        final CalendarRenderer calendarRenderer = new CalendarRenderer();

        HtmlCalendar htmlCalendar = new HtmlCalendar();

        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar)).isEqualTo(PREPEND
            + "datetimepicker({icons: {time: 'glyphicon glyphicon-time',date: 'glyphicon glyphicon-calendar',up: 'glyphicon glyphicon-chevron-up',down: 'glyphicon glyphicon-chevron-down'}})");

        htmlCalendar.setSideBySide(true);
        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar)).isEqualTo(PREPEND
            + "datetimepicker({sideBySide: true,icons: {time: 'glyphicon glyphicon-time',date: 'glyphicon glyphicon-calendar',up: 'glyphicon glyphicon-chevron-up',down: 'glyphicon glyphicon-chevron-down'}})");

        htmlCalendar.setSideBySide(false);
        htmlCalendar.setIconTime("fa fa-clock-o");
        htmlCalendar.setIconDate("fa fa-calendar");
        htmlCalendar.setIconUp("fa fa-arrow-up");
        htmlCalendar.setIconDown("fa fa-arrow-down");
        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar))
            .isEqualTo(PREPEND + "datetimepicker({icons: {time: 'fa fa-clock-o',date: 'fa fa-calendar',up: 'fa fa-arrow-up',down: 'fa fa-arrow-down'}})");

        htmlCalendar.setLocale("ru");
        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar))
            .isEqualTo(PREPEND + "datetimepicker({locale: 'ru',icons: {time: 'fa fa-clock-o',date: 'fa fa-calendar',up: 'fa fa-arrow-up',down: 'fa fa-arrow-down'}})");

        htmlCalendar.setLocale("");
        htmlCalendar.setFormat("DD-MM-YYYY");
        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar)).isEqualTo(
            PREPEND + "datetimepicker({format: 'DD-MM-YYYY',icons: {time: 'fa fa-clock-o',date: 'fa fa-calendar',up: 'fa fa-arrow-up',down: 'fa fa-arrow-down'}})");

        htmlCalendar.setPickDate(true);
        htmlCalendar.setPickTime(true);
        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar)).isEqualTo(
            PREPEND + "datetimepicker({format: 'DD-MM-YYYY',icons: {time: 'fa fa-clock-o',date: 'fa fa-calendar',up: 'fa fa-arrow-up',down: 'fa fa-arrow-down'}})");

        htmlCalendar.setFormat(null);
        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar))
            .isEqualTo(PREPEND + "datetimepicker({icons: {time: 'fa fa-clock-o',date: 'fa fa-calendar',up: 'fa fa-arrow-up',down: 'fa fa-arrow-down'}})");

        htmlCalendar.setPickTime(false);
        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar))
            .isEqualTo(PREPEND + "datetimepicker({format: 'L',icons: {time: 'fa fa-clock-o',date: 'fa fa-calendar',up: 'fa fa-arrow-up',down: 'fa fa-arrow-down'}})");

        htmlCalendar.setPickDate(false);
        htmlCalendar.setPickTime(true);
        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar))
            .isEqualTo(PREPEND + "datetimepicker({format: 'LT',icons: {time: 'fa fa-clock-o',date: 'fa fa-calendar',up: 'fa fa-arrow-up',down: 'fa fa-arrow-down'}})");

        htmlCalendar.setViewMode(HtmlCalendarViewMode.YEARS);
        assertThat(calendarRenderer.createJQueryPluginCall(htmlCalendar)).isEqualTo(PREPEND
            + "datetimepicker({format: 'LT',viewMode: 'years',icons: {time: 'fa fa-clock-o',date: 'fa fa-calendar',up: 'fa fa-arrow-up',down: 'fa fa-arrow-down'}})");
    }
}