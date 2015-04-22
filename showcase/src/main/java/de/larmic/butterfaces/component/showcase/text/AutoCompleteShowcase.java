package de.larmic.butterfaces.component.showcase.text;

import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.model.text.AutoCompleteModel;

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
        xhtmlCodeExample.appendInnerContent("                value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                tooltip=\"" + this.getTooltip() + "\"");
        xhtmlCodeExample.appendInnerContent("                styleClass=\"" + this.getStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        if (this.isValidation()) {
            xhtmlCodeExample.appendInnerContent("            <f:validateLength minimum=\"2\" maximum=\"10\"/>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:text>", true);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        generateDemoCSS(codeExamples);
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
