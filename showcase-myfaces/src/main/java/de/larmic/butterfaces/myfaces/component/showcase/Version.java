package de.larmic.butterfaces.myfaces.component.showcase;

/**
 * CDI wrapper of bean. Don't know why but bean of shared module is not accessible from jsf view.
 */
@javax.inject.Named
@javax.faces.view.ViewScoped
public class Version extends de.larmic.butterfaces.component.showcase.Version {
}
