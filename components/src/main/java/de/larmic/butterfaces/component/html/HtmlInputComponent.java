package de.larmic.butterfaces.component.html;

public interface HtmlInputComponent {

	String getTooltip();

	boolean isReadonly();

	boolean getFloating();

    // TODO remove
    @Deprecated
    boolean getDisableDefaultStyleClasses();

    String getComponentStyleClass();

    String getInputStyleClass();

    String getLabelStyleClass();

	boolean isRequired();

	boolean isValid();

	String getLabel();

	Object getValue();

	String getClientId();
}
