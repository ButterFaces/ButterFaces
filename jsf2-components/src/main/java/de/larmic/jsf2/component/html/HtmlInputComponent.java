package de.larmic.jsf2.component.html;

public interface HtmlInputComponent {

	String getTooltip();

	boolean getReadonly();

	boolean getFloating();

	String getStyle();

	String getStyleClass();

	boolean isRequired();

	boolean isValid();

	String getLabel();

	Object getValue();

	String getClientId();

}
