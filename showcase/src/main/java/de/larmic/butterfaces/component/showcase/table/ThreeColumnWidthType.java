package de.larmic.butterfaces.component.showcase.table;

public enum ThreeColumnWidthType {
	NONE("No custom width"), PX("50px / 30px / 20px"), PERCENT("10% / 80% / 10%"), RELATIVE("5* / 1* / 7*");
	public final String label;

	private ThreeColumnWidthType(final String label) {
		this.label = label;
	}
}
