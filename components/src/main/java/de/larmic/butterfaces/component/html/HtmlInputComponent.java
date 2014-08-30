package de.larmic.butterfaces.component.html;

public interface HtmlInputComponent {

    String getTooltip();

    boolean isReadonly();

    boolean getHideLabel();

    boolean getFloating();

    String getComponentStyleClass();

    String getStyleClass();

    String getInputStyleClass();

    String getLabelStyleClass();

    boolean isRequired();

    boolean isValid();

    String getLabel();

    Object getValue();

    String getClientId();
}
