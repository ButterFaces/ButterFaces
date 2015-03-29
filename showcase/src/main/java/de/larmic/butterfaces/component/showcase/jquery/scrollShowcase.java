package de.larmic.butterfaces.component.showcase.jquery;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.CssCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class scrollShowcase extends AbstractCodeShowcase implements Serializable {

    @Override
    public void buildCodeExamples(List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <div class=\"scroll-buttons alert alert-info\">");
        xhtmlCodeExample.appendInnerContent("            <h3>Click here for scrolling...</h3>");
        xhtmlCodeExample.appendInnerContent("            <div class=\"btn-group pull-right\" role=\"group\">");
        xhtmlCodeExample.appendInnerContent("                <!-- 60px is showcase header offset -->");
        xhtmlCodeExample.appendInnerContent("                <span class=\"btn btn-default btn-xs\"");
        xhtmlCodeExample.appendInnerContent("                      onclick=\"$('.scrollable-item').butterScrollToFirst(-60);\">top</span>");
        xhtmlCodeExample.appendInnerContent("                <span class=\"btn btn-default btn-xs\"");
        xhtmlCodeExample.appendInnerContent("                      onclick=\"$('.middle').butterScrollToFirst(-60);\">middle</span>");
        xhtmlCodeExample.appendInnerContent("                <span class=\"btn btn-default btn-xs\"");
        xhtmlCodeExample.appendInnerContent("                      onclick=\"$('.scrollable-item').butterScrollToLast(-60);\">bottom</span>");
        xhtmlCodeExample.appendInnerContent("            </div>");
        xhtmlCodeExample.appendInnerContent("        </div>\n");

        xhtmlCodeExample.appendInnerContent("        <div class=\"top scrollable-item alert alert-info\">Top scrollable item</div>\n");

        xhtmlCodeExample.appendInnerContent("        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit....</p>");
        xhtmlCodeExample.appendInnerContent("        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit....</p>");
        xhtmlCodeExample.appendInnerContent("        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit....</p>\n");

        xhtmlCodeExample.appendInnerContent("        <div class=\"top scrollable-item alert alert-info\">Middle scrollable item</div>\n");

        xhtmlCodeExample.appendInnerContent("        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit....</p>");
        xhtmlCodeExample.appendInnerContent("        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit....</p>");
        xhtmlCodeExample.appendInnerContent("        <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit....</p>\n");

        xhtmlCodeExample.appendInnerContent("        <div class=\"top scrollable-item alert alert-info\">Bottom scrollable item</div>\n");

        xhtmlCodeExample.appendInnerContent("\n        /* add jQuery and jQuery plugins to html head part */");
        xhtmlCodeExample.appendInnerContent("        <b:activateLibraries />", false);

        final CssCodeExample cssCodeExample = new CssCodeExample();

        cssCodeExample.addCss(".scroll-buttons", "position: fixed;", "right: 75px;", "bottom: 0;");

        codeExamples.add(xhtmlCodeExample);
        codeExamples.add(cssCodeExample);
    }
}
