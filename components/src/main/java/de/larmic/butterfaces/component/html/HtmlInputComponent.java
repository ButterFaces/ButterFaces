package de.larmic.butterfaces.component.html;

import de.larmic.butterfaces.component.html.feature.*;

import javax.faces.component.UIComponent;

public interface HtmlInputComponent extends StyleClass, Validation, Readonly, Required, Label, HideLabel, SupportedFacets {

    Object getValue();

    String getClientId();

    UIComponent getFacet(final String name);
}
