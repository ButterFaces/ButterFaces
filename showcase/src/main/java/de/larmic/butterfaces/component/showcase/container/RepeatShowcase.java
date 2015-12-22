package de.larmic.butterfaces.component.showcase.container;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.table.DemoPojo;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class RepeatShowcase extends AbstractCodeShowcase implements Serializable {

    private final List<DemoPojo> values;

    public RepeatShowcase() {
        values = new ArrayList<>();

        for (int i = 0; i < 123; i++) {
            values.add(new DemoPojo(i, "a" + i, "b" + i));
        }
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("\n        <b:repeat id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                  value=\"#{myBean.values}\"");
        xhtmlCodeExample.appendInnerContent("                  var=\"value\"");
        xhtmlCodeExample.appendInnerContent("                  status=\"status\"");
        xhtmlCodeExample.appendInnerContent("                  rendered=\"" + this.isRendered() + "\">");

        xhtmlCodeExample.appendInnerContent("           <div class=\"row\">");
        xhtmlCodeExample.appendInnerContent("              <div class=\"col-md-1\">#{value.id}</div>");
        xhtmlCodeExample.appendInnerContent("              <b:text value=\"#{value.a}\" hideLabel=\"true\" styleClass=\"col-md-2\">");
        xhtmlCodeExample.appendInnerContent("                 <f:ajax execute=\"@this\" event=\"change\" render=\"@this\"/>");
        xhtmlCodeExample.appendInnerContent("                 <f:validateRequired />");
        xhtmlCodeExample.appendInnerContent("              </b:text>");
        xhtmlCodeExample.appendInnerContent("              <div class=\"col-md-1\">#{value.b}</div>");
        xhtmlCodeExample.appendInnerContent("              <div class=\"col-md-3\">#{value.actualDate}</div>");
        xhtmlCodeExample.appendInnerContent("              <div class=\"col-md-5\">#{status}</div>");
        xhtmlCodeExample.appendInnerContent("           </div>");

        xhtmlCodeExample.appendInnerContent("        </b:repeat>");

        codeExamples.add(xhtmlCodeExample);
    }

    public List<DemoPojo> getValues() {
        return values;
    }
}
