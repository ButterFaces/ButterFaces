package de.larmic.butterfaces.component.showcase.container;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.comboBox.Episode;
import de.larmic.butterfaces.component.showcase.comboBox.EpisodeConverter;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.CssCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.table.DemoPojo;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class RepeatShowcase extends AbstractCodeShowcase implements Serializable {

    private RepeatExampleType selectedExampleType = RepeatExampleType.STARGATE;

    private final List<DemoPojo> values;

    public RepeatShowcase() {
        values = new ArrayList<>();

        for (int i = 0; i < 123; i++) {
            values.add(new DemoPojo(i, "a" + i, "b" + i));
        }
    }

    public void play() {

    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("\n        <b:commandLink value=\"Refresh\">");
        xhtmlCodeExample.appendInnerContent("           <f:ajax execute=\"@this\" render=\"repeat\"/>");
        xhtmlCodeExample.appendInnerContent("        </b:commandLink>");

        xhtmlCodeExample.appendInnerContent("\n        <h:panelGroup id=\"repeat\" layout=\"block\" class=\"repeat-simple-list\">");
        xhtmlCodeExample.appendInnerContent("           <b:repeat id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                     value=\"#{myBean.values}\"");
        xhtmlCodeExample.appendInnerContent("                     var=\"value\"");
        xhtmlCodeExample.appendInnerContent("                     status=\"status\"");
        xhtmlCodeExample.appendInnerContent("                     rendered=\"" + this.isRendered() + "\">");

        xhtmlCodeExample.appendInnerContent("              <div class=\"row\">");
        xhtmlCodeExample.appendInnerContent("                 <div class=\"col-md-1\">#{value.id}</div>");
        xhtmlCodeExample.appendInnerContent("                 <b:text value=\"#{value.a}\" hideLabel=\"true\" styleClass=\"col-md-2\">");
        xhtmlCodeExample.appendInnerContent("                    <f:ajax execute=\"@this\" event=\"change\" render=\"@this\"/>");
        xhtmlCodeExample.appendInnerContent("                    <f:validateRequired />");
        xhtmlCodeExample.appendInnerContent("                 </b:text>");
        xhtmlCodeExample.appendInnerContent("                 <div class=\"col-md-1\">#{value.b}</div>");
        xhtmlCodeExample.appendInnerContent("                 <div class=\"col-md-3\">#{value.actualDate}</div>");
        xhtmlCodeExample.appendInnerContent("                 <div class=\"col-md-5\">#{status}</div>");
        xhtmlCodeExample.appendInnerContent("              </div>");

        xhtmlCodeExample.appendInnerContent("           </b:repeat>");
        xhtmlCodeExample.appendInnerContent("        </h:panelGroup>");

        codeExamples.add(xhtmlCodeExample);

        if (selectedExampleType == RepeatExampleType.SIMPLE) {
            final CssCodeExample cssCodeExample = new CssCodeExample();
            cssCodeExample.addCss(".repeat-simple-list", "max-height: 400px;", "overflow: auto;");
            codeExamples.add(cssCodeExample);
        }
    }

    public List<Episode> getValues() {
        return EpisodeConverter.EPISODES;
    }

    public List<DemoPojo> getDemoPojos() {
        return values;
    }

    public List<SelectItem> getExampleTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final RepeatExampleType type : RepeatExampleType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public RepeatExampleType getSelectedExampleType() {
        return selectedExampleType;
    }

    public void setSelectedExampleType(RepeatExampleType selectedExampleType) {
        this.selectedExampleType = selectedExampleType;
    }
}
