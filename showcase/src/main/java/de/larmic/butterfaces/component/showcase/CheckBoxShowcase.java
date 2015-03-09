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
public class CheckBoxShowcase extends AbstractInputShowcase implements Serializable {

    private String description = "some description";

    private boolean autoFocus;

    @Override
    protected Object initValue() {
        return false;
    }

    @Override
    public String getReadableValue() {
        return (Boolean) this.getValue() ? "Ja" : "Nein";
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:checkBox id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                    label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                    value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                    description=\"" + description + "\"");
        xhtmlCodeExample.appendInnerContent("                    tooltip=\"" + this.getTooltip() + "\"");
        xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + this.getStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                    readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                    required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                    autoFocus=\"" + this.isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                    rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        xhtmlCodeExample.appendInnerContent("        </b:checkBox>", false);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

        generateDemoCSS(codeExamples);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }
}
