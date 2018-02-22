package org.butterfaces.component.showcase.textarea;

import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.butterfaces.component.showcase.AbstractInputShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.textarea.example.TextAreaWebXmlExample;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.showcase.textarea.example.TextAreaWebXmlExample;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.util.StringUtils;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class TextAreaShowcase extends AbstractInputShowcase implements Serializable {

    private Integer maxLength;
    private Boolean expandable;
    private boolean autoFocus;

    private String placeholder = DEFAULT_TEXT_PLACEHOLDER;

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

        xhtmlCodeExample.appendInnerContent("        <b:textArea id=\"input\"");
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
        xhtmlCodeExample.appendInnerContent("                    autoFocus=\"" + this.isAutoFocus() + "\"");

        if (this.getMaxLength() != null) {
            xhtmlCodeExample.appendInnerContent("                    maxLength=\"" + this.getMaxLength() + "\"");
        }

        if (Boolean.TRUE.equals(this.getExpandable())) {
            xhtmlCodeExample.appendInnerContent("                    expandable=\"" + this.getExpandable() + "\"");
        }

        xhtmlCodeExample.appendInnerContent("                    rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:textArea>", false);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        generateDemoCSS(codeExamples);
        codeExamples.add(new TextAreaWebXmlExample());
    }

    public Integer getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(final Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Boolean getExpandable() {
        return expandable;
    }

    public void setExpandable(Boolean expandable) {
        this.expandable = expandable;
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

    public boolean isWarningRendered() {
        return Boolean.TRUE.equals(expandable) && (maxLength != null && maxLength > 0);
    }
}
