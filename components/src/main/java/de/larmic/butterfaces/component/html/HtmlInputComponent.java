package de.larmic.butterfaces.component.html;

public interface HtmlInputComponent {

	String getTooltip();

	boolean isReadonly();

	boolean getFloating();

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
