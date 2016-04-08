/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.radioBox.examples;

import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class RadioBoxListOfEnumsMyBeanExample extends JavaCodeExample {

    public RadioBoxListOfEnumsMyBeanExample() {
        super("MyBean.java", "mybean", "radioBox.demo", "MyBean", true);

        this.addImport("javax.faces.view.ViewScoped");
        this.addImport("javax.inject.Named");

        this.appendInnerContent("    private FooType selectedValue;\n");

        this.appendInnerContent("    public List<FooType> getValues() {");
        this.appendInnerContent("        return Arrays.asList(FooType.values());");
        this.appendInnerContent("    }");

        this.appendInnerContent("\n    // GETTER + SETTER (selectedValue)");
    }

}
