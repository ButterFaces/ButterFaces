package org.butterfaces.component.showcase.tooltip;

public enum TooltipType {
	A("<a />"), COMMAND_LINK("<b:commandLink />"), TEXT("<b:text />"), READONLY_TEXT("<b:text readonly=\"true\"/>");
	public final String label;

	private TooltipType(final String label) {
		this.label = label;
	}
}
