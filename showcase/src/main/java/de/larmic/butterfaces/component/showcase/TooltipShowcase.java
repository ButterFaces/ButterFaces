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

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:tooltip id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                   rendered=\"" + this.isRendered() + "\">");
        xhtmlCodeExample.appendInnerContent("            Lorem ipsum dolor sit amet, consectetuer ...");
        xhtmlCodeExample.appendInnerContent("        </b:tooltip>", false);

        codeExamples.add(xhtmlCodeExample);
    }

}
