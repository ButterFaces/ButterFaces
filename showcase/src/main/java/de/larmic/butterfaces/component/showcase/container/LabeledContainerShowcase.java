package de.larmic.butterfaces.component.showcase.container;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class LabeledContainerShowcase extends AbstractCodeShowcase implements Serializable {

    private LabeledContainerExampleType exampleType = LabeledContainerExampleType.TEXT;
    private String label = "label";
    private boolean hideLabel;

    @Override
    public void buildCodeExamples(List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);
        xhtmlCodeExample.setWrappedByForm(false);

        xhtmlCodeExample.appendInnerContent("\n        <b:labeledContainer id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                            label=\"" + this.label + "\"");
        xhtmlCodeExample.appendInnerContent("                            hideLabel=\"" + hideLabel + "\"");
        xhtmlCodeExample.appendInnerContent("                            rendered=\"" + this.isRendered() + "\">");

        if (exampleType == LabeledContainerExampleType.TEXT) {
            xhtmlCodeExample.appendInnerContent("            Lorem ipsum dolor sit amet, consectetuer ...");
        } else if (exampleType == LabeledContainerExampleType.LINK) {
            xhtmlCodeExample.appendInnerContent("            <b:commandLink value=\"click me\" />");
        } else if (exampleType == LabeledContainerExampleType.SECTION) {
            xhtmlCodeExample.appendInnerContent("            <b:section label=\"demo\">");
            xhtmlCodeExample.appendInnerContent("                Lorem ipsum dolor sit amet, consectetuer ...");
            xhtmlCodeExample.appendInnerContent("            </b:section>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:labeledContainer>");

        codeExamples.add(xhtmlCodeExample);
    }

    public List<SelectItem> getExampleTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final LabeledContainerExampleType type : LabeledContainerExampleType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public boolean isHideLabel() {
        return hideLabel;
    }

    public void setHideLabel(boolean hideLabel) {
        this.hideLabel = hideLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LabeledContainerExampleType getExampleType() {
        return exampleType;
    }

    public void setExampleType(LabeledContainerExampleType exampleType) {
        this.exampleType = exampleType;
    }
}
