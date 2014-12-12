package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class SectionShowcase extends AbstractCodeShowcase implements Serializable {

    private String badgeText = null;
    private String label = "label";

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:section id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                   label=\"" + this.label + "\"");
        xhtmlCodeExample.appendInnerContent("                   badgeText=\"" + this.badgeText + "\"");
        xhtmlCodeExample.appendInnerContent("                   rendered=\"" + this.isRendered() + "\">");
        xhtmlCodeExample.appendInnerContent("            Lorem ipsum dolor sit amet, consectetuer ...");
        xhtmlCodeExample.appendInnerContent("        </b:section>");
        xhtmlCodeExample.appendInnerContent("        Lorem ipsum dolor sit amet, consectetuer ...", false);

        codeExamples.add(xhtmlCodeExample);
    }

    public String getBadgeText() {
        return badgeText;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
