/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.radioBox.examples;

import de.larmic.butterfaces.component.showcase.example.EnumCodeExample;

/**
 * @author Lars Michaelis
 */
public class RadioBoxFooTypeExample extends EnumCodeExample {

    public RadioBoxFooTypeExample() {
        super("FooType.java", "fooType", "radioBox.demo", "FooType", null);


        this.appendInnerContent("    FOO_TYPE_1(\"FooTypeEnumLabel1\"),");
        this.appendInnerContent("    FOO_TYPE_2(\"FooTypeEnumLabel2\"),");
        this.appendInnerContent("    FOO_TYPE_3(\"FooTypeEnumLabel3\");\n");

        this.appendInnerContent("    private String label;\n");

        this.appendInnerContent("    FooType(final String label) {");
        this.appendInnerContent("       this.label = label;");
        this.appendInnerContent("    }");

        this.appendInnerContent("\n    // GETTER (label)");
    }

}
