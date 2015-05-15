package de.larmic.butterfaces.component.showcase;

import java.io.Serializable;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class StaticLabelWidthShowcase implements Serializable {

    private static final String CALENDAR_VALUE = "05/11/2015 6:45 PM";
    private static final String TEXT_VALUE = "This is a test string.";
    private static final String TEXT_AREA_VALUE = "This is a text\nfor text areas\n\nwith line breaks.";
    private static final String CHECKBOX_DESCRIPTION = "Some description";
    private static final boolean CHECKBOX_VALUE = true;

    public String getTextValue() {
        return TEXT_VALUE;
    }

    public String getTextAreaValue() {
        return TEXT_AREA_VALUE;
    }

    public String getCheckboxDescription() {
        return CHECKBOX_DESCRIPTION;
    }

    public boolean isCheckboxValue() {
        return CHECKBOX_VALUE;
    }

    public String getCalendarValue() {
        return CALENDAR_VALUE;
    }
}
