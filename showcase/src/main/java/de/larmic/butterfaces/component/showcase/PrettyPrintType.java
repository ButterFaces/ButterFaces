package de.larmic.butterfaces.component.showcase;

public enum PrettyPrintType {
	HTML("html/xhtml"), XML("xml"), JAVA("java");
	public final String label;

	private PrettyPrintType(final String label) {
		this.label = label;
	}
}
