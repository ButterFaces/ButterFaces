package org.butterfaces.component.showcase.action;

import org.butterfaces.component.showcase.AbstractCodeShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
public class DefaultActionShowcase extends AbstractCodeShowcase implements Serializable {

    private String buttonClick;
    private String value;

    public void clickFirst() {
        buttonClick = "click first button";
    }

    public void clickLast() {
        buttonClick = "click last button";
    }

    public void clickDefault() {
        buttonClick = "click default button";
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:text value=\"#{myBean.value}\" />\n");
        xhtmlCodeExample.appendInnerContent("        <b:commandLink id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                       action=\"#{myBean.submit}\"");
        xhtmlCodeExample.appendInnerContent("                       value=\"click me\"");
        xhtmlCodeExample.appendInnerContent("                       styleClass=\"btn btn-primary\">");
        xhtmlCodeExample.appendInnerContent("            <f:ajax execute=\"@form\" render=\"@form\"/>");
        xhtmlCodeExample.appendInnerContent("            <b:defaultAction rendered=\"" + this.isRendered() + "\"/>");
        xhtmlCodeExample.appendInnerContent("        </b:commandLink>", false);

        codeExamples.add(xhtmlCodeExample);

        final JavaCodeExample myBean = new JavaCodeExample("defaultaction.demo", "MyBean", true);

        myBean.appendInnerContent("    private String value;\n");
        myBean.appendInnerContent("    public void submit() {");
        myBean.appendInnerContent("        // implement me");
        myBean.appendInnerContent("    }\n");
        myBean.appendInnerContent("    // getter and setter");

        codeExamples.add(myBean);

    }

    public String getButtonClick() {
        return buttonClick;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.buttonClick = null;
    }
}

