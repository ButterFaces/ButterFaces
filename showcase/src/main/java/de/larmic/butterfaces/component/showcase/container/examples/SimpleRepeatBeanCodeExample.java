/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.container.examples;

import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class SimpleRepeatBeanCodeExample extends JavaCodeExample {

    public SimpleRepeatBeanCodeExample() {
        super("MyBean.java", "mybean", "repeat.demo", "MyBean", true);

        this.addImport("import javax.faces.view.ViewScoped");
        this.addImport("import javax.inject.Named");

        this.appendInnerContent("    private List<DemoPojo> pojos = new ArrayList<DemoPojo>();\n");

        this.appendInnerContent("    public List<DemoPojo> getValues() {");
        this.appendInnerContent("       if(pojos.isEmpty()) {");
        this.appendInnerContent("          for (int i = 0; i < 123; i++) {");
        this.appendInnerContent("             pojos.add(new DemoPojo(i, \"a\" + i, \"b\" + i));");
        this.appendInnerContent("          }");
        this.appendInnerContent("       }\n");
        this.appendInnerContent("        return pojos;");
        this.appendInnerContent("    }");
    }
}
