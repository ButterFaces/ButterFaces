package de.larmic.jsf2.component.showcase;

public enum StyleSheetType {
	DEFAULT("default"), DISABLE_DEFAULT("disableDefaultStyleClasses");
	public final String label;

	private StyleSheetType(final String label) {
		this.label = label;
	}
}
