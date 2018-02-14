/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.tree;

/**
 * @author Lars Michaelis
 */
public class Image {

    private final String path;
    private final String extension;
    private final String name;

    public Image(String path, String name, String extension) {
        this.path = path;
        this.extension = extension;
        this.name = name;
    }

    public String getUrl() {
        return path + name + "." + extension;
    }
}
