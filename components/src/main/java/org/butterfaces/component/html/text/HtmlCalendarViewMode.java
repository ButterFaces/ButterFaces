package org.butterfaces.component.html.text;

public enum HtmlCalendarViewMode {
    TIMES,
    DAYS,
    MONTHS,
    YEARS,
    DECADES;

    public String getValue() {
        return name().toLowerCase();
    }
}
