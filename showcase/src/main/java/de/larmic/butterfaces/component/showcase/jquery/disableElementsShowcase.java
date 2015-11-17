package de.larmic.butterfaces.component.showcase.jquery;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class disableElementsShowcase extends AbstractCodeShowcase implements Serializable {

    @Override
    public void buildCodeExamples(List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <span class=\"btn btn-default\"");
        xhtmlCodeExample.appendInnerContent("              onclick=\"new ButterFaces.Overlay(0, false, '.disableElementsContainer').show();\">");
        xhtmlCodeExample.appendInnerContent("            Disable");
        xhtmlCodeExample.appendInnerContent("        </span>");
        xhtmlCodeExample.appendInnerContent("        <span class=\"btn btn-default\"");
        xhtmlCodeExample.appendInnerContent("              onclick=\"new ButterFaces.Overlay(0, false, '.disableElementsContainer').hide();\">");
        xhtmlCodeExample.appendInnerContent("            Enable");
        xhtmlCodeExample.appendInnerContent("        </span>");

        xhtmlCodeExample.appendInnerContent("\n        <div class=\"alert alert-info disableElementsContainer\">");
        xhtmlCodeExample.appendInnerContent("            <div>Some text...</div>");
        xhtmlCodeExample.appendInnerContent("            <div>Some other text...</div>");
        xhtmlCodeExample.appendInnerContent("            <div><input value=\"An input field\"></input></div>");
        xhtmlCodeExample.appendInnerContent("        </div>");

        xhtmlCodeExample.appendInnerContent("\n        /* add jQuery and jQuery plugins to html head part */");
        xhtmlCodeExample.appendInnerContent("        <b:activateLibraries />", false);

        codeExamples.add(xhtmlCodeExample);
    }
}
