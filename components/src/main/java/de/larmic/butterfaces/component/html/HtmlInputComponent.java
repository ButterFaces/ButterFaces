package de.larmic.butterfaces.component.html;

import de.larmic.butterfaces.component.html.feature.*;

public interface HtmlInputComponent extends Style, StyleClass, Validation, Readonly, Required, Label, HideLabel, SupportedFacets {

    Object getValue();

    String getClientId();

}
