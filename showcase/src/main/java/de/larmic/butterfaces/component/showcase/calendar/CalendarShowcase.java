package de.larmic.butterfaces.component.showcase.calendar;

import de.larmic.butterfaces.util.StringUtils;
import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class CalendarShowcase extends AbstractInputShowcase implements Serializable {

    private CalendarIconType selectedIconType = CalendarIconType.DEFAULT;
    private String glyphiconDate = null;
    private String glyphiconTime = null;
    private String glyphiconUp = null;
    private String glyphiconDown = null;
    private String placeholder = "Enter date or click icon...";
    private boolean autoFocus;
    private boolean pickDate = true;
    private boolean pickTime = true;
    private boolean sideBySise = false;
    private String language = "en";

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
        xhtmlCodeExample.appendInnerContent("                    sideBySide=\"" + sideBySise + "\"");
        xhtmlCodeExample.appendInnerContent("                    language=\"" + language + "\"");
        xhtmlCodeExample.appendInnerContent("                    glyphiconDate=\"" + glyphiconDate + "\"");
        xhtmlCodeExample.appendInnerContent("                    glyphiconTime=\"" + glyphiconTime + "\"");
        xhtmlCodeExample.appendInnerContent("                    glyphiconUp=\"" + glyphiconUp + "\"");
        xhtmlCodeExample.appendInnerContent("                    glyphiconDown=\"" + glyphiconDown + "\"");
        xhtmlCodeExample.appendInnerContent("                    placeholder=\"" + getPlaceholder() + "\"");
        xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + getStyleClass() + "\"");
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public CalendarIconType getSelectedIconType() {
        return selectedIconType;
    }

    public void setSelectedIconType(CalendarIconType selectedIconType) {
        this.selectedIconType = selectedIconType;
        switch (selectedIconType) {
            case DEFAULT:
                glyphiconDate = null;
                glyphiconTime = null;
                glyphiconUp = null;
                glyphiconDown = null;
                break;
            case AWESOME:
                glyphiconDate = "fa fa-calendar";
                glyphiconTime = "fa fa-clock-o";
                glyphiconUp = "fa fa-chevron-up";
                glyphiconDown = "fa fa-chevron-down";
                break;
        }
    }

    public String getGlyphiconTime() {
        return glyphiconTime;
    }

    public String getGlyphiconDate() {
        return glyphiconDate;
    }

    public String getGlyphiconUp() {
        return glyphiconUp;
    }

    public String getGlyphiconDown() {
        return glyphiconDown;
    }

    public boolean isSideBySise() {
        return sideBySise;
    }

    public void setSideBySise(boolean sideBySise) {
        this.sideBySise = sideBySise;
    }
}
