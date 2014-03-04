package de.larmic.jsf2.component.html;

public interface HtmlInputComponent {

	String getTooltip();

	boolean isReadonly();

	boolean getFloating();

    String getComponentStyleClass();

    String getInputStyleClass();

    String getLabelStyleClass();

	boolean isRequired();

	boolean isValid();

	String getLabel();

	Object getValue();

	String getClientId();
}
