package de.larmic.butterfaces.component.showcase.table;

public enum TableModelType {
	NONE("No table model"), DEFAULT_MODEL("Default table model");
	public final String label;

	private TableModelType(final String label) {
		this.label = label;
	}
}
