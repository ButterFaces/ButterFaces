/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.tree.examples;

import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.tree.ShowcaseTreeNode;
import org.butterfaces.component.showcase.tree.TreeBoxExampleType;
import org.butterfaces.component.showcase.tree.TreeIconType;
import org.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class TreeBoxListOfStringsJavaExample extends JavaCodeExample {

    public TreeBoxListOfStringsJavaExample() {
        super("MyBean.java", "mybean", "treeBox.demo", "MyBean", true);

        this.addImport("javax.faces.view.ViewScoped");
        this.addImport("javax.inject.Named");

        this.appendInnerContent("    private String selectedValue;\n");

        this.appendInnerContent("    public List<String> getValues() {");
        this.appendInnerContent("        return Arrays.asList(\"Inbox\", \"Drafts\", \"Sent\", \"Tagged\", \"Folders\", \"Trash\");");
        this.appendInnerContent("    }");

        this.appendInnerContent("\n    // GETTER + SETTER (selectedValue)");
    }
}
