package de.larmic.butterfaces.component.showcase.table;

public enum TwoColumnWidthType {
	NONE("No custom width"), PX("50px / 50px"), PERCENT("10% / 90%"), RELATIVE("5* / 1*");
	public final String label;

	private TwoColumnWidthType(final String label) {
		this.label = label;
	}
}
