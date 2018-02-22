package org.butterfaces.component.html;

import org.butterfaces.component.html.feature.*;
import org.butterfaces.component.html.feature.*;

public interface HtmlInputComponent extends Style, StyleClass, Validation, Readonly, Required, Label, HideLabel, SupportedFacets {

    Object getValue();

    String getClientId();

}
