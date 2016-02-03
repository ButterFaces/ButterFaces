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
public class TreeBoxEpisodesJavaExample extends JavaCodeExample {

    public TreeBoxEpisodesJavaExample(String subPackage) {
        super("Episode.java", "episode", subPackage, "Episode", false);

        this.appendInnerContent("    private int numberInSeries;");
        this.appendInnerContent("    private String title;");
        this.appendInnerContent("    private String writtenBy;");
        this.appendInnerContent("    private String originalAirDate;");
        this.appendInnerContent("    private String image;");
        this.appendInnerContent("\n    // [...] getter + setter\n");
        this.appendInnerContent("    @Override");
        this.appendInnerContent("    public String toString() {");
        this.appendInnerContent("       return title;");
        this.appendInnerContent("    }");
    }
}
