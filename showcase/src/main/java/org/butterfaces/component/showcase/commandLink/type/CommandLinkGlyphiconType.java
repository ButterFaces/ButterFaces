package org.butterfaces.component.showcase.commandLink.type;

public enum CommandLinkGlyphiconType {
	DEFAULT("No glyphicon", null),
	BOOTSTRAP("Bootstrap example", "glyphicon glyphicon-thumbs-up glyphicon-lg"),
    AWESOME("Font-Awesome example", "fa fa-language fa-lg");
	public final String label;
	public final String value;

	CommandLinkGlyphiconType(final String label, final String value) {
		this.label = label;
		this.value = value;
	}
}
