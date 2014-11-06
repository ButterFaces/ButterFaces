package de.larmic.butterfaces.component.showcase;

public enum TreeSelectionAjaxType {
	NONE("No ajax selection"), AJAX("ajax enabled"), AJAX_DISABLED("ajax disabled");
	public final String label;

	private TreeSelectionAjaxType(final String label) {
		this.label = label;
	}
}
