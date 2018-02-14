package org.butterfaces.component.showcase.jquery;

import org.butterfaces.component.showcase.AbstractCodeShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class ItemFilterFieldShowcase extends AbstractCodeShowcase implements Serializable {

    @Override
    public void buildCodeExamples(List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <div class=\"form-inline small-margin-bottom\">");
        xhtmlCodeExample.appendInnerContent("            <div class=\"form-group\">");
        xhtmlCodeExample.appendInnerContent("                <label for=\"myItemFilterField\" class=\"control-label\">Filter:</label>");
        xhtmlCodeExample.appendInnerContent("                <input type=\"text\" id=\"myItemFilterField\" class=\"form-control\" data-filterable-item-container=\"#itemContainer\"/>");
        xhtmlCodeExample.appendInnerContent("            </div>");
        xhtmlCodeExample.appendInnerContent("        </div>");
        xhtmlCodeExample.appendInnerContent("        <div class=\"well\" id=\"itemContainer\">");
        xhtmlCodeExample.appendInnerContent("            <div class=\"filterable-item\">");
        xhtmlCodeExample.appendInnerContent("                Some filterable item...");
        xhtmlCodeExample.appendInnerContent("            </div>");
        xhtmlCodeExample.appendInnerContent("            <div class=\"filterable-item\">");
        xhtmlCodeExample.appendInnerContent("                Another <b>one</b>.");
        xhtmlCodeExample.appendInnerContent("            </div>");
        xhtmlCodeExample.appendInnerContent("            <div class=\"filterable-item\">");
        xhtmlCodeExample.appendInnerContent("                And yet another one with slightly more text.");
        xhtmlCodeExample.appendInnerContent("            </div>");
        xhtmlCodeExample.appendInnerContent("            <div>");
        xhtmlCodeExample.appendInnerContent("                <i>This one will not get filtered out because it does not have the class <code>filterable-item</code>.</i>");
        xhtmlCodeExample.appendInnerContent("            </div>");
        xhtmlCodeExample.appendInnerContent("        </div>");

        xhtmlCodeExample.appendInnerContent("\n        <script type=\"text/javascript\">");
        xhtmlCodeExample.appendInnerContent("            // activate jquery plugin");
        xhtmlCodeExample.appendInnerContent("            jQuery('#myItemFilterField').butterItemFilterField();");
        xhtmlCodeExample.appendInnerContent("        </script>");

        xhtmlCodeExample.appendInnerContent("\n        /* add jQuery and jQuery plugins to html head part */");
        xhtmlCodeExample.appendInnerContent("        <b:activateLibraries />", false);

        codeExamples.add(xhtmlCodeExample);
    }
}
