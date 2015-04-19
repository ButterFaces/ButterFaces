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
public class TooltipShowcase extends AbstractCodeShowcase implements Serializable {

    private String title = "custom title";
    private String trigger = "hover";
    private String placement;

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <a id=\"btn\">"+trigger+" me!</a>");

        xhtmlCodeExample.appendInnerContent("        <b:tooltip id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                   for=\"btn\"");
        xhtmlCodeExample.appendInnerContent("                   title=\"" + title + "\"");
        xhtmlCodeExample.appendInnerContent("                   trigger=\"" + trigger + "\"");
        xhtmlCodeExample.appendInnerContent("                   placement=\"" + placement + "\"");
        xhtmlCodeExample.appendInnerContent("                   rendered=\"" + this.isRendered() + "\">");
        xhtmlCodeExample.appendInnerContent("            <strong>some text stuff</strong>");
        xhtmlCodeExample.appendInnerContent("            <br />");
        xhtmlCodeExample.appendInnerContent("            <h:panelGroup>");
        xhtmlCodeExample.appendInnerContent("                Lorem ipsum dolor sit amet, consectetuer ...");
        xhtmlCodeExample.appendInnerContent("            </h:panelGroup>");
        xhtmlCodeExample.appendInnerContent("        </b:tooltip>", false);

        codeExamples.add(xhtmlCodeExample);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }
}
