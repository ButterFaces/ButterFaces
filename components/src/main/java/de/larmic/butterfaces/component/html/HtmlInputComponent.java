package de.larmic.butterfaces.component.html;

public interface HtmlInputComponent {

    String getTooltip();

    boolean isReadonly();

    boolean getHideLabel();

    boolean getFloating();

    String getComponentStyleClass();

    String getInputStyleClass();

    String getLabelStyleClass();

    boolean isRequired();

    boolean isValid();

    String getLabel();

    void setLabel(final String label);

    Object getValue();

    String getClientId();
}
