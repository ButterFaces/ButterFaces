package org.butterfaces.component.showcase;

import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.util.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class NumberShowcase extends AbstractInputShowcase implements Serializable {

    private String placeholder = DEFAULT_NUMBER_PLACEHOLDER;
    private String min;
    private String max;
    private String step;
    private boolean autoFocus;

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

        xhtmlCodeExample.appendInnerContent("        <b:number id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                  label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                  hideLabel=\"" + isHideLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                  value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                  placeholder=\"" + this.getPlaceholder() + "\"");
        xhtmlCodeExample.appendInnerContent("                  min=\"" + this.getMin() + "\"");
        xhtmlCodeExample.appendInnerContent("                  max=\"" + this.getMax() + "\"");
        xhtmlCodeExample.appendInnerContent("                  step=\"" + this.getStep() + "\"");
        if (this.getStyleClass() == StyleClass.BIG_LABEL) {
            xhtmlCodeExample.appendInnerContent("                  styleClass=\"" + this.getSelectedStyleClass() + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                  readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                  required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                  disabled=\"" + this.isDisabled() + "\"");
        xhtmlCodeExample.appendInnerContent("                  autoFocus=\"" + this.isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                  rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "change");

        if (this.isValidation()) {
            xhtmlCodeExample.appendInnerContent("            <f:validateLength minimum=\"2\" maximum=\"10\"/>");
        }

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:number>", false);

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

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
