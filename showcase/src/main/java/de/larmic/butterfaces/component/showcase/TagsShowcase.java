package de.larmic.butterfaces.component.showcase;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class TagsShowcase extends AbstractInputShowcase implements Serializable {

    private String placeholder = "Enter text...";
    private boolean autoFocus;
    private boolean allowDuplicates;
    private boolean trimValue;
    private Integer maxTags;
    private Integer maxChars;

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

        xhtmlCodeExample.appendInnerContent("        <b:tags id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                allowDuplicates=\"" + allowDuplicates + "\"");
        //  plugin error trim value does not work correclty
        //xhtmlCodeExample.appendInnerContent("                trimValue=\"" + trimValue + "\"");
        xhtmlCodeExample.appendInnerContent("                maxTags=\"" + maxTags + "\"");
        xhtmlCodeExample.appendInnerContent("                maxChars=\"" + maxChars + "\"");
        xhtmlCodeExample.appendInnerContent("                tooltip=\"" + this.getTooltip() + "\"");
        xhtmlCodeExample.appendInnerContent("                placeholder=\"" + this.getPlaceholder() + "\"");
        xhtmlCodeExample.appendInnerContent("                styleClass=\"" + this.getStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                autoFocus=\"" + this.isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        if (this.isValidation()) {
            xhtmlCodeExample.appendInnerContent("            <f:validateLength minimum=\"2\" maximum=\"10\"/>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:tags>", false);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        generateDemoCSS(codeExamples);
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    public boolean isAllowDuplicates() {
        return allowDuplicates;
    }

    public void setAllowDuplicates(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    public boolean isTrimValue() {
        return trimValue;
    }

    public void setTrimValue(boolean trimValue) {
        this.trimValue = trimValue;
    }

    public Integer getMaxTags() {
        return maxTags;
    }

    public void setMaxTags(Integer maxTags) {
        this.maxTags = maxTags;
    }

    public Integer getMaxChars() {
        return maxChars;
    }

    public void setMaxChars(Integer maxChars) {
        this.maxChars = maxChars;
    }
}
