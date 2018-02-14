/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.container.examples;

import org.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class NestedRepeatBeanCodeExample extends JavaCodeExample {

    public NestedRepeatBeanCodeExample() {
        super("MyBean.java", "mybean", "repeat.demo", "MyBean", true);

        this.addImport("import javax.faces.view.ViewScoped");
        this.addImport("import javax.inject.Named");

        this.appendInnerContent("    public List<String> getOuterValues() {");
        this.appendInnerContent("       return Arrays.asList(\"A\", \"B\", \"C\");");
        this.appendInnerContent("    }");

        this.appendInnerContent("\n    public List<String> getInnerValueForOuterValue(String outerValue) {");
        this.appendInnerContent("       rreturn Arrays.asList(outerValue + \"1\", outerValue + \"2\", outerValue + \"3\");");
        this.appendInnerContent("    }");
    }
}
