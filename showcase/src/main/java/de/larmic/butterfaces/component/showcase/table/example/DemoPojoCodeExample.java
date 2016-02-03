/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.table.example;

import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class DemoPojoCodeExample extends JavaCodeExample {

    public DemoPojoCodeExample(String subPackage) {
        super("DemoPojo.java", "DemoPojo", subPackage, "DemoPojo", false);

        appendInnerContent("    private final long id;");
        appendInnerContent("    private final String a;");
        appendInnerContent("    private final String b;");
        appendInnerContent("    private final String date;\n");
        appendInnerContent("    public DemoPojo(final long id, final String a, final String b) {");
        appendInnerContent("        this.id = id;");
        appendInnerContent("        this.a = a;");
        appendInnerContent("        this.b = b;");
        appendInnerContent("        this.date = new java.util.Date();");
        appendInnerContent("    }\n");
        appendInnerContent("    // getter");
    }

}
