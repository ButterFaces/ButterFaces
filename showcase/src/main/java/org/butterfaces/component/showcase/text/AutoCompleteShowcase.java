package org.butterfaces.component.showcase.text;

import org.butterfaces.component.showcase.AbstractInputShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.model.text.AutoCompleteModel;
import org.butterfaces.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class AutoCompleteShowcase extends AbstractInputShowcase implements Serializable {

    private final List<String> autoCompleteValues = new ArrayList<>();

    private boolean ajaxEnabled = true;

    @PostConstruct
    public void init() {
        autoCompleteValues.add("test");
        autoCompleteValues.add("tetest");
        autoCompleteValues.add("test1 ButterFaces");
        autoCompleteValues.add("test2");
        autoCompleteValues.add("ButterFaces");
        autoCompleteValues.add("ButterFaces JSF");
        autoCompleteValues.add("ButterFaces Mojarra");
        autoCompleteValues.add("ButterFaces Component");
        autoCompleteValues.add("JSF");
        autoCompleteValues.add("JSF 2");
        autoCompleteValues.add("JSF 2.2");
    }

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public String getReadableValue() {
        return (String) this.getValue();
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("\n        <b:text id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                value=\"" + this.getValue() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        xhtmlCodeExample.appendInnerContent("            <b:autoComplete autoComplete=\"#{myBean}\"", true);
        xhtmlCodeExample.appendInnerContent("                            rendered=\"" + this.isRendered() + "\"/>");

        xhtmlCodeExample.appendInnerContent("        </b:text>", true);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);
        codeExamples.add(createMyBeanCodeExample());
    }

    private JavaCodeExample createMyBeanCodeExample() {
        final JavaCodeExample myBean = new JavaCodeExample("MyBean.java", "mybean", "autocomplete.demo", "MyBean", true);

        myBean.addImport("javax.faces.view.ViewScoped");
        myBean.addImport("javax.inject.Named");
        myBean.addImport("\norg.butterfaces.model.text.AutoCompleteModel");

        myBean.addInterfaces("AutoCompleteModel");

        myBean.appendInnerContent("    public List<String> autoComplete(Object value) {");
        myBean.appendInnerContent("        // TODO implement me...");
        myBean.appendInnerContent("    }");

        return myBean;
    }

    public AutoCompleteModel getAutoCompleteModel() {
        return new AutoCompleteModel() {
            @Override
            public List<String> autoComplete(Object value) {
                final List<String> values = new ArrayList<>();

                if (StringUtils.isNotEmpty(value.toString())) {
                    for (String autoCompleteValue : autoCompleteValues) {
                        if (autoCompleteValue.toLowerCase().contains(value.toString().toLowerCase())) {
                            values.add(autoCompleteValue);
                        }
                    }
                }

                return values;
            }
        };
    }

    @Override
    public boolean isAjax() {
        return ajaxEnabled;
    }

    public boolean isAjaxEnabled() {
        return ajaxEnabled;
    }

    public void setAjaxEnabled(boolean ajaxEnabled) {
        this.ajaxEnabled = ajaxEnabled;
    }
}
