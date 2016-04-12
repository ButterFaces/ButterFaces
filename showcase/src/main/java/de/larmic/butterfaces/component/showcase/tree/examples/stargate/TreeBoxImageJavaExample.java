/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.tree.examples.stargate;

import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class TreeBoxImageJavaExample extends JavaCodeExample {

    public TreeBoxImageJavaExample(String subPackage) {
        super("Image.java", "image", subPackage, "Image", false);

        this.appendInnerContent("    private static final String IMAGE_PATH = \"resources/images/treebox/\";\n");

        this.appendInnerContent("    private final String path;");
        this.appendInnerContent("    private final String extension;");
        this.appendInnerContent("    private final String name;");

        this.appendInnerContent("\n    public Image(String path, String name, String extension) {");
        this.appendInnerContent("       this.path = path;");
        this.appendInnerContent("       this.extension = extension;");
        this.appendInnerContent("       this.name = name;");
        this.appendInnerContent("    }");

        this.appendInnerContent("\n    public String getUrl() {");
        this.appendInnerContent("       return path + name + \".\" + extension;");
        this.appendInnerContent("    }");
    }
}
