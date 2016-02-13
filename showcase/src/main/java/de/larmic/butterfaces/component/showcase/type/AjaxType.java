package de.larmic.butterfaces.component.showcase.type;

public enum AjaxType {
	NONE("No ajax"), THIS("@this"), COMPONENT("component");
	public final String label;

	private AjaxType(final String label) {
		this.label = label;
	}
}
