/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.tags;

/**
 * @author Lars Michaelis
 */
public enum TagsExampleType {
	STRINGS("default"),
	EXPERIMENTAL("experimental");
	public final String label;

	TagsExampleType(final String label) {
		this.label = label;
	}
}
