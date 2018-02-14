/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.tree;

/**
 * @author Lars Michaelis
 */
public enum TreeIconType {
	NONE("No icon"), IMAGE("Image icon"), GLYPHICON("Glyphicon icon");
	public final String label;

	private TreeIconType(final String label) {
		this.label = label;
	}
}
