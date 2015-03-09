package de.larmic.butterfaces.component.html;

import java.util.List;

import javax.faces.component.UIComponent;

public interface HtmlInputComponent {

    String getTooltip();

    boolean isReadonly();

    boolean isHideLabel();

    String getStyleClass();

    boolean isRequired();

    boolean isValid();

    String getLabel();

    Object getValue();

    String getClientId();

    UIComponent getFacet(final String name);

    List<InputComponentFacet> getSupportedFacets();
}
