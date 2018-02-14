package org.butterfaces.component.showcase.textarea;

import org.butterfaces.component.showcase.AbstractInputShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.textarea.example.MarkDownWebXmlExample;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.showcase.textarea.example.MarkDownWebXmlExample;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class MarkdownShowcase extends AbstractInputShowcase implements Serializable {

    private Integer maxLength;
    private boolean autoFocus;

    private String language = "en";
    private String placeholder = DEFAULT_TEXT_PLACEHOLDER;

    @Override
    protected Object initValue() {
        return "Hallo & < > ";
    }

    @Override
    public String getReadableValue() {
        return (String) this.getValue();
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:markdown id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                    label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                    hideLabel=\"" + isHideLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                    value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                    tooltip=\"" + this.getTooltip() + "\"");
        if (this.getStyleClass() == StyleClass.BIG_LABEL) {
            xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + this.getSelectedStyleClass() + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                    readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                    required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                    disabled=\"" + this.isDisabled() + "\"");
        xhtmlCodeExample.appendInnerContent("                    placeholder=\"" + placeholder + "\"");
        xhtmlCodeExample.appendInnerContent("                    language=\"" + language + "\"");
        xhtmlCodeExample.appendInnerContent("                    autoFocus=\"" + this.isAutoFocus() + "\"");

        if (this.getMaxLength() != null) {
            xhtmlCodeExample.appendInnerContent("                    maxLength=\"" + this.getMaxLength() + "\"");
        }

        xhtmlCodeExample.appendInnerContent("                    rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:markdown>", false);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        generateDemoCSS(codeExamples);
        codeExamples.add(new MarkDownWebXmlExample());
    }

    public Integer getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
