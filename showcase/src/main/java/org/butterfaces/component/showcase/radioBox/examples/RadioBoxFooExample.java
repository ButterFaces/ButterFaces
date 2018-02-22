/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.radioBox.examples;

import org.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class RadioBoxFooExample extends JavaCodeExample {

    public RadioBoxFooExample() {
        super("Foo.java", "foo", "radioBox.demo", "Foo", false);


        this.appendInnerContent("    private String key;");
        this.appendInnerContent("    private String value;\n");

        this.appendInnerContent("    public Foo(final String key, final String value) {");
        this.appendInnerContent("       this.key = key;");
        this.appendInnerContent("       this.value = value;");
        this.appendInnerContent("    }");

        this.appendInnerContent("\n    // GETTER (key and value)");

        this.appendInnerContent("\n    @Override");
        this.appendInnerContent("    public String toString() {");
        this.appendInnerContent("       return this.value;");
        this.appendInnerContent("    }");
    }

}
