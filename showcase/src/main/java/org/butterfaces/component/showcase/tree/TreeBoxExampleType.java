/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.tree;

/**
 * @author Lars Michaelis
 */
public enum TreeBoxExampleType {
	ROOT_NODE("simple tree"),
	NODES("list of nodes"),
	STRINGS("list of strings"),
	ENUMS("list of enums"),
	OBJECTS("list of objects"),
	TEMPLATE("custom template");
	public final String label;

	TreeBoxExampleType(final String label) {
		this.label = label;
	}
}
