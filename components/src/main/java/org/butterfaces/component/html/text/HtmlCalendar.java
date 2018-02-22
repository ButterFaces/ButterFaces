package org.butterfaces.component.html.text;

import org.butterfaces.component.html.HtmlInputComponent;
import org.butterfaces.component.html.InputComponentFacet;
import org.butterfaces.component.html.feature.AutoFocus;
import org.butterfaces.component.html.feature.Placeholder;
import org.butterfaces.component.html.feature.AutoFocus;
import org.butterfaces.component.html.feature.Placeholder;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;
import java.util.Collections;
import java.util.List;

@ResourceDependencies({
    @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
    @ResourceDependency(library = "butterfaces-dist-css", name = "dist-butterfaces-bootstrap.css", target = "head"),
    @ResourceDependency(library = "butterfaces-dist-js", name = "butterfaces-guid.js", target = "head"),
    @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-jquery.js", target = "head"),
    @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party.js", target = "head"),
    @ResourceDependency(library = "butterfaces-dist-bundle-dev-js", name = "butterfaces-third-party-bootstrap.js", target = "head")
})
@FacesComponent(HtmlCalendar.COMPONENT_TYPE)
public class HtmlCalendar extends HtmlText implements HtmlInputComponent, AutoFocus, Placeholder {

    public static final String COMPONENT_TYPE = "org.butterfaces.component.calendar";
    public static final String COMPONENT_FAMILY = "org.butterfaces.component.family";
    public static final String RENDERER_TYPE = "org.butterfaces.component.renderkit.html_basic.CalendarRenderer";

    protected static final String PROPERTY_ICON_TIME = "iconTime";
    protected static final String PROPERTY_ICON_DATE = "iconDate";
    protected static final String PROPERTY_ICON_UP = "iconUp";
    protected static final String PROPERTY_ICON_DOWN = "iconDown";
    protected static final String PROPERTY_PICK_DATE = "pickDate";
    protected static final String PROPERTY_PICK_TIME = "pickTime";
    protected static final String PROPERTY_LOCALE = "locale";
    protected static final String PROPERTY_FORMAT = "format";
    protected static final String PROPERTY_SIDE_BY_SIDE = "sideBySide";
    protected static final String PROPERTY_VIEW_MODE = "viewMode";

    public HtmlCalendar() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public List<InputComponentFacet> getSupportedFacets() {
        return Collections.singletonList(InputComponentFacet.CALENDAR);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public boolean isPickDate() {
        return Boolean.valueOf(getStateHelper().eval(PROPERTY_PICK_DATE, Boolean.TRUE).toString());
    }

    public void setPickDate(boolean pickDate) {
        getStateHelper().put(PROPERTY_PICK_DATE, pickDate);
    }

    public boolean isPickTime() {
        return Boolean.valueOf(getStateHelper().eval(PROPERTY_PICK_TIME, Boolean.TRUE).toString());
    }

    public void setPickTime(boolean pickTime) {
        getStateHelper().put(PROPERTY_PICK_TIME, pickTime);
    }

    public boolean isSideBySide() {
        return Boolean.valueOf(getStateHelper().eval(PROPERTY_SIDE_BY_SIDE, Boolean.FALSE).toString());
    }

    public void setSideBySide(boolean sideBySide) {
        getStateHelper().put(PROPERTY_SIDE_BY_SIDE, sideBySide);
    }

    public HtmlCalendarViewMode getViewMode() {
        return (HtmlCalendarViewMode) this.getStateHelper().eval(PROPERTY_VIEW_MODE);
    }

    public void setViewMode(HtmlCalendarViewMode viewMode) {
        getStateHelper().put(PROPERTY_VIEW_MODE, viewMode);
    }

    public String getLocale() {
        return (String) this.getStateHelper().eval(PROPERTY_LOCALE);
    }

    public void setLocale(String locale) {
        this.updateStateHelper(PROPERTY_LOCALE, locale);
    }

    public String getFormat() {
        return (String) this.getStateHelper().eval(PROPERTY_FORMAT);
    }

    public void setFormat(String format) {
        this.updateStateHelper(PROPERTY_FORMAT, format);
    }

    public String getIconTime() {
        return (String) this.getStateHelper().eval(PROPERTY_ICON_TIME);
    }

    public void setIconTime(final String icon) {
        this.updateStateHelper(PROPERTY_ICON_TIME, icon);
    }

    public String getIconDate() {
        return (String) this.getStateHelper().eval(PROPERTY_ICON_DATE);
    }

    public void setIconDate(final String icon) {
        this.updateStateHelper(PROPERTY_ICON_DATE, icon);
    }

    public String getIconUp() {
        return (String) this.getStateHelper().eval(PROPERTY_ICON_UP);
    }

    public void setIconUp(final String icon) {
        this.updateStateHelper(PROPERTY_ICON_UP, icon);
    }

    public String getIconDown() {
        return (String) this.getStateHelper().eval(PROPERTY_ICON_DOWN);
    }

    public void setIconDown(final String icon) {
        this.updateStateHelper(PROPERTY_ICON_DOWN, icon);
    }

    @Override
    public String getValidationErrorPlacement() {
        return (String) this.getStateHelper().eval(PROPERTY_VALIDATION_ERROR_PLACEMENT, "top");
    }
}
