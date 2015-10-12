package de.larmic.butterfaces.component.showcase.table;

public enum FourthColumnWidthType {
	NONE("No custom width"), PX("50px / 30px / 10px / 10px"), PERCENT("10% / 65% / 15%  / 10%"), RELATIVE("5* / 1* / 7* / 1*");
	public final String label;

	private FourthColumnWidthType(final String label) {
		this.label = label;
	}
}
