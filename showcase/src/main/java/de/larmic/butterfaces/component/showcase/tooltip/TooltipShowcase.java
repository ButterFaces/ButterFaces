package de.larmic.butterfaces.component.showcase.tooltip;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class TooltipShowcase extends AbstractCodeShowcase implements Serializable {

    private TooltipType selectedTooltTipType = TooltipType.A;
    private String title = "custom title";
    private String trigger = "hover";
    private String placement;

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        if (selectedTooltTipType == TooltipType.A) {
            xhtmlCodeExample.appendInnerContent("        <a id=\"btn\" class=\"btn btn-default\">" + trigger + " me!</a>");

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
        } else {
            xhtmlCodeExample.appendInnerContent("        <b:commandLink value=\"" + trigger + " me!\"");
            xhtmlCodeExample.appendInnerContent("                       styleClass=\"btn btn-default\">");
            xhtmlCodeExample.appendInnerContent("            <b:tooltip title=\"" + title + "\"");
            xhtmlCodeExample.appendInnerContent("                       trigger=\"" + trigger + "\"");
            xhtmlCodeExample.appendInnerContent("                       placement=\"" + placement + "\"");
            xhtmlCodeExample.appendInnerContent("                       rendered=\"" + this.isRendered() + "\">");
            xhtmlCodeExample.appendInnerContent("                <strong>some text stuff</strong>");
            xhtmlCodeExample.appendInnerContent("                <br />");
            xhtmlCodeExample.appendInnerContent("                <h:panelGroup>");
            xhtmlCodeExample.appendInnerContent("                    Lorem ipsum dolor sit amet, consectetuer ...");
            xhtmlCodeExample.appendInnerContent("                </h:panelGroup>");
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
            xhtmlCodeExample.appendInnerContent("        </b:commandLink>", false);
        }

        codeExamples.add(xhtmlCodeExample);
    }

    public List<SelectItem> getTooltipTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final TooltipType type : TooltipType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
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

    public TooltipType getSelectedTooltTipType() {
        return selectedTooltTipType;
    }

    public void setSelectedTooltTipType(TooltipType selectedTooltTipType) {
        this.selectedTooltTipType = selectedTooltTipType;
    }
}
