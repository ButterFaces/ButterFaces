package org.butterfaces.component.showcase.calendar;

import org.butterfaces.component.html.text.HtmlCalendarViewMode;
import org.butterfaces.component.showcase.AbstractInputShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.type.Locale;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.model.tree.EnumTreeBoxWrapper;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.html.text.HtmlCalendarViewMode;
import org.butterfaces.component.showcase.type.Locale;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private Locale locale = null;
    private String format = null;
    private HtmlCalendarViewMode viewMode = null;

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
        if (locale != null) {
            xhtmlCodeExample.appendInnerContent("                    locale=\"" + locale.value + "\"");
        }
        if (StringUtils.isNotEmpty(format)) {
            xhtmlCodeExample.appendInnerContent("                    format=\"" + format + "\"");
        }
        if (viewMode != null) {
            xhtmlCodeExample.appendInnerContent("                    viewMode=\"" + viewMode.getValue() + "\"");
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
        if (this.getStyleClass() == StyleClass.BIG_LABEL) {
            xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + getSelectedStyleClass() + "\"");
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

    public List<EnumTreeBoxWrapper> getCalendarIconTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final CalendarIconType type : CalendarIconType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public List<EnumTreeBoxWrapper> getLocaleExamples() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final Locale type : Locale.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public List<HtmlCalendarViewMode> getViewModes() {
        return Arrays.asList(HtmlCalendarViewMode.values());
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

    public String getSelectedLocale() {
        return locale == null ? null : locale.value;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public HtmlCalendarViewMode getViewMode() {
        return viewMode;
    }

    public void setViewMode(HtmlCalendarViewMode viewMode) {
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
