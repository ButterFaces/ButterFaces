package de.larmic.butterfaces.component.showcase.action;

import de.larmic.butterfaces.component.showcase.AbstractShowcaseSingleCodeComponent;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
public class DefaultActionShowcaseSingleCodeComponent extends AbstractShowcaseSingleCodeComponent implements Serializable {

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
    protected void addJavaCode(final StringBuilder sb) {
        sb.append("package de.larmic.defaultaction.demo;\n\n");
        sb.append("import javax.faces.view.ViewScoped;\n");
        sb.append("import javax.inject.Named;\n\n");

        sb.append("@ViewScoped\n");
        sb.append("@Named\n");
        sb.append("public class MyBean implements Serializable {\n\n");
        sb.append("    private String value;\n\n");
        sb.append("    public void submit() {\n");
        sb.append("        // implement me\n");
        sb.append("    }\n\n");
        sb.append("    // getter and setter\n\n");
        sb.append("}\n\n");
    }

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

        sb.append("        <b:test value=\"#{myBean.value}\" />\n\n");
        sb.append("        <b:commandLink id=\"input\"\n");

        this.appendString("action", "#{myBean.submit}", sb);
        this.appendString("value", "click me", sb);
        this.appendString("styleClass", "btn btn-primary", sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);
        sb.append("            <f:ajax execute=\"@form\" render=\"@form\"/>\n");
        sb.append("            <b:defaultAction rendered=\"" + this.isRendered() + "\"/>\n");
        sb.append("        </b:commandLink>");

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                       ";
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

