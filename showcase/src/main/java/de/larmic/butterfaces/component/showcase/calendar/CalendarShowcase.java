package de.larmic.butterfaces.component.showcase.calendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.util.StringUtils;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class CalendarShowcase extends AbstractInputShowcase implements Serializable {

    private CalendarIconType selectedIconType = CalendarIconType.DEFAULT;
    private String iconDate = null;
    private String iconTime = null;
    private String iconUp = null;
    private String iconDown = null;
    private String placeholder = "Enter date or click icon...";
    private boolean autoFocus;
    private boolean pickDate = true;
    private boolean pickTime = true;
    private boolean sideBySide = false;
    private String locale = "en";
    private String format = null;
    private String viewMode = null;

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public String getReadableValue() {
        return (String) this.getValue();
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final boolean useFontAwesome = selectedIconType == CalendarIconType.AWESOME;

        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(useFontAwesome);

        xhtmlCodeExample.appendInnerContent("        <b:calendar id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                    label=\"" + getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                    hideLabel=\"" + isHideLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                    value=\"" + getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                    pickDate=\"" + pickDate + "\"");
        xhtmlCodeExample.appendInnerContent("                    pickTime=\"" + pickTime + "\"");
        xhtmlCodeExample.appendInnerContent("                    sideBySide=\"" + sideBySide + "\"");
        if (StringUtils.isNotEmpty(locale)) {
            xhtmlCodeExample.appendInnerContent("                    locale=\"" + locale + "\"");
        }
        if (StringUtils.isNotEmpty(format)) {
            xhtmlCodeExample.appendInnerContent("                    format=\"" + format + "\"");
        }
        if (StringUtils.isNotEmpty(viewMode)) {
            xhtmlCodeExample.appendInnerContent("                    viewMode=\"" + viewMode + "\"");
        }
        if (StringUtils.isNotEmpty(iconDate)) {
            xhtmlCodeExample.appendInnerContent("                    iconDate=\"" + iconDate + "\"");
        }
        if (StringUtils.isNotEmpty(iconTime)) {
            xhtmlCodeExample.appendInnerContent("                    iconTime=\"" + iconTime + "\"");
        }
        if (StringUtils.isNotEmpty(iconUp)) {
            xhtmlCodeExample.appendInnerContent("                    iconUp=\"" + iconUp + "\"");
        }
        if (StringUtils.isNotEmpty(iconDown)) {
            xhtmlCodeExample.appendInnerContent("                    iconDown=\"" + iconDown + "\"");
        }
        if (StringUtils.isNotEmpty(getPlaceholder())) {
            xhtmlCodeExample.appendInnerContent("                    placeholder=\"" + getPlaceholder() + "\"");
        }
        if (StringUtils.isNotEmpty(getStyleClass())) {
            xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + getStyleClass() + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                    readonly=\"" + isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                    required=\"" + isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                    disabled=\"" + this.isDisabled() + "\"");
        xhtmlCodeExample.appendInnerContent("                    autoFocus=\"" + isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                    rendered=\"" + isRendered() + "\">");

        addAjaxTag(xhtmlCodeExample, "change");

        if (isValidation()) {
            xhtmlCodeExample.appendInnerContent("            <f:validateLength minimum=\"2\" maximum=\"10\"/>");
        }

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:calendar>", false);

        addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        generateDemoCSS(codeExamples);
    }

    public List<SelectItem> getCalendarIconTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final CalendarIconType type : CalendarIconType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    public boolean isPickDate() {
        return pickDate;
    }

    public void setPickDate(boolean pickDate) {
        this.pickDate = pickDate;
    }

    public boolean isPickTime() {
        return pickTime;
    }

    public void setPickTime(boolean pickTime) {
        this.pickTime = pickTime;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public CalendarIconType getSelectedIconType() {
        return selectedIconType;
    }

    public void setSelectedIconType(CalendarIconType selectedIconType) {
        this.selectedIconType = selectedIconType;
        switch (selectedIconType) {
            case DEFAULT:
                iconDate = null;
                iconTime = null;
                iconUp = null;
                iconDown = null;
                break;
            case AWESOME:
                iconDate = "fa fa-calendar";
                iconTime = "fa fa-clock-o";
                iconUp = "fa fa-chevron-up";
                iconDown = "fa fa-chevron-down";
                break;
        }
    }

    public String getIconTime() {
        return iconTime;
    }

    public String getIconDate() {
        return iconDate;
    }

    public String getIconUp() {
        return iconUp;
    }

    public String getIconDown() {
        return iconDown;
    }

    public boolean isSideBySide() {
        return sideBySide;
    }

    public void setSideBySide(boolean sideBySide) {
        this.sideBySide = sideBySide;
    }
}
