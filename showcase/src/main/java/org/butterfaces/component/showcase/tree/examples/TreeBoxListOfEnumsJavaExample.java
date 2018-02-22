/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.tree.examples;

import org.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class TreeBoxListOfEnumsJavaExample extends JavaCodeExample {

    public TreeBoxListOfEnumsJavaExample() {
        super("MyBean.java", "mybean", "treeBox.demo", "MyBean", true);

        this.addImport("org.butterfaces.model.tree.Node");
        this.addImport("org.butterfaces.model.tree.DefaultNodeImpl");
        this.addImport("org.butterfaces.model.tree.EnumTreeBoxWrapper");
        this.addImport("javax.faces.view.ViewScoped");
        this.addImport("javax.inject.Named");

        this.appendInnerContent("    private List<EnumTreeBoxWrapper> values = new ArrayList<>();\n");

        this.appendInnerContent("    // selectedValue is enum instead of wrapper!");
        this.appendInnerContent("    private TreeBoxExampleEnum selectedValue;\n");

        this.appendInnerContent("    // Return list of EnumTreeBoxWrapper");
        this.appendInnerContent("    public List<EnumTreeBoxWrapper> getValues() {");
        this.appendInnerContent("        if (values.isEmpty()) {");
        this.appendInnerContent("            values.add(new EnumTreeBoxWrapper(TreeBoxExampleEnum.MAIL, \"E-Mail\"));");
        this.appendInnerContent("            values.add(new EnumTreeBoxWrapper(TreeBoxExampleEnum.PDF, \"PDF\"));");
        this.appendInnerContent("            values.add(new EnumTreeBoxWrapper(TreeBoxExampleEnum.TXT, \"plain text\"));");
        this.appendInnerContent("        }");
        this.appendInnerContent("        return values;");
        this.appendInnerContent("    }");

        this.appendInnerContent("\n    // GETTER + SETTER (selectedValue)");
    }
}
