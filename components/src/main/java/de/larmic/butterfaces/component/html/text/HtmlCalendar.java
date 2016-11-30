package de.larmic.butterfaces.component.html.text;

import java.util.Arrays;
import java.util.List;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.FacesComponent;

import de.larmic.butterfaces.component.html.HtmlInputComponent;
import de.larmic.butterfaces.component.html.InputComponentFacet;
import de.larmic.butterfaces.component.html.feature.AutoFocus;
import de.larmic.butterfaces.component.html.feature.Placeholder;

@ResourceDependencies({
        @ResourceDependency(library = "butterfaces-dist-css", name = "butterfaces-default.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "jquery.js", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.css", target = "head"),
        @ResourceDependency(library = "butterfaces-dist-bower", name = "bootstrap.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "01-moment-with-locales.min.js", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-datetimepicker.min.css", target = "head"),
        @ResourceDependency(library = "butterfaces-external", name = "bootstrap-datetimepicker.min.js", target = "head")
})
@FacesComponent(HtmlCalendar.COMPONENT_TYPE)
public class HtmlCalendar extends HtmlText implements HtmlInputComponent, AutoFocus, Placeholder {

    public static final String COMPONENT_TYPE = "de.larmic.butterfaces.component.calendar";
    public static final String COMPONENT_FAMILY = "de.larmic.butterfaces.component.family";
    public static final String RENDERER_TYPE = "de.larmic.butterfaces.component.renderkit.html_basic.CalendarRenderer";

    protected static final String PROPERTY_GLYPHICON_TIME = "glyphiconTime";
    protected static final String PROPERTY_GLYPHICON_DATE = "glyphiconDate";
    protected static final String PROPERTY_GLYPHICON_UP = "glyphiconUp";
    protected static final String PROPERTY_GLYPHICON_DOWN = "glyphiconDown";
    protected static final String PROPERTY_PICK_DATE = "pickDate";
    protected static final String PROPERTY_PICK_TIME = "pickTime";
    protected static final String PROPERTY_LANGUAGE = "language";
    protected static final String PROPERTY_FORMAT = "format";
    protected static final String PROPERTY_SIDE_BY_SIDE = "sideBySide";

    public HtmlCalendar() {
        super();
        this.setRendererType(RENDERER_TYPE);
    }

    @Override
    public List<InputComponentFacet> getSupportedFacets() {
        return Arrays.asList(InputComponentFacet.CALENDAR);
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
        ;
    }

    public boolean isPickTime() {
        return Boolean.valueOf(getStateHelper().eval(PROPERTY_PICK_TIME, Boolean.TRUE).toString());
    }

    public void setPickTime(boolean pickTime) {
        getStateHelper().put(PROPERTY_PICK_TIME, pickTime);
        ;
    }

    public boolean isSideBySide() {
        return Boolean.valueOf(getStateHelper().eval(PROPERTY_SIDE_BY_SIDE, Boolean.FALSE).toString());
    }

    public void setSideBySide(boolean sideBySide) {
        getStateHelper().put(PROPERTY_SIDE_BY_SIDE, sideBySide);
    }

    public String getLanguage() {
        return (String) this.getStateHelper().eval(PROPERTY_LANGUAGE);
    }

    public void setLanguage(String language) {
        this.updateStateHelper(PROPERTY_LANGUAGE, language);
    }

    public String getFormat() {
        return (String) this.getStateHelper().eval(PROPERTY_FORMAT);
    }

    public void setFormat(String format) {
        this.updateStateHelper(PROPERTY_FORMAT, format);
    }

    public String getGlyphiconTime() {
        return (String) this.getStateHelper().eval(PROPERTY_GLYPHICON_TIME);
    }

    public void setGlyphiconTime(final String glyphicon) {
        this.updateStateHelper(PROPERTY_GLYPHICON_TIME, glyphicon);
    }

    public String getGlyphiconDate() {
        return (String) this.getStateHelper().eval(PROPERTY_GLYPHICON_DATE);
    }

    public void setGlyphiconDate(final String glyphicon) {
        this.updateStateHelper(PROPERTY_GLYPHICON_DATE, glyphicon);
    }

    public String getGlyphiconUp() {
        return (String) this.getStateHelper().eval(PROPERTY_GLYPHICON_UP);
    }

    public void setGlyphiconUp(final String glyphicon) {
        this.updateStateHelper(PROPERTY_GLYPHICON_UP, glyphicon);
    }

    public String getGlyphiconDown() {
        return (String) this.getStateHelper().eval(PROPERTY_GLYPHICON_DOWN);
    }

    public void setGlyphiconDown(final String glyphicon) {
        this.updateStateHelper(PROPERTY_GLYPHICON_DOWN, glyphicon);
    }

}
