package de.larmic.butterfaces.myfaces.component.showcase.text;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * CDI wrapper of bean. Don't know why but bean of shared module is not accessible from jsf view.
 */
@Named
@ViewScoped
public class CalendarShowcase extends de.larmic.butterfaces.component.showcase.calendar.CalendarShowcase {
}
