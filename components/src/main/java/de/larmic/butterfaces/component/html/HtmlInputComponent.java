package de.larmic.butterfaces.component.html;

import javax.faces.component.UIComponent;
import java.util.List;

public interface HtmlInputComponent {

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
