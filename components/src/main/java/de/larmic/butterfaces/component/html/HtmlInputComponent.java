package de.larmic.butterfaces.component.html;

import javax.faces.component.UIComponent;
import java.util.List;

public interface HtmlInputComponent {

    String getTooltip();

    boolean isReadonly();

    boolean isHideLabel();

    String getStyleClass();

    String getInputStyleClass();

    String getLabelStyleClass();

    boolean isRequired();

    boolean isValid();

    String getLabel();

    Object getValue();

    String getClientId();

    UIComponent getFacet(final String name);

    List<InputComponentFacet> getSupportedFacets();
}
