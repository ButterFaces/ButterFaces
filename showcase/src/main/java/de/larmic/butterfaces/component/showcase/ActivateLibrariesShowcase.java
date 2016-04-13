package de.larmic.butterfaces.component.showcase;

import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.WebXmlCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class ActivateLibrariesShowcase extends AbstractCodeShowcase implements Serializable {

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);
        xhtmlCodeExample.setWrappedByForm(false);

        xhtmlCodeExample.appendInnerContent("        <b:activateLibraries id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                             rendered=\"" + this.isRendered() + "\">");
        xhtmlCodeExample.appendInnerContent("        </b:activateLibraries>", false);

        codeExamples.add(xhtmlCodeExample);
        codeExamples.add(createWebXmlExample());
    }

    private AbstractCodeExample createWebXmlExample() {
        final WebXmlCodeExample webXmlCodeExample = new WebXmlCodeExample("web.xml", "webxml");

        webXmlCodeExample.appendInnerContent("  <!-- If you want to enable compressed and minified resources -->");
        webXmlCodeExample.appendInnerContent("  <!-- (http://yui.github.io/yuicompressor/ is used) -->");
        webXmlCodeExample.appendInnerContent("  <!-- default is false -->");
        webXmlCodeExample.appendInnerContent("  <context-param>");
        webXmlCodeExample.appendInnerContent("     <param-name>org.butterfaces.useCompressedResources</param-name>");
        webXmlCodeExample.appendInnerContent("     <param-value>true</param-value>");
        webXmlCodeExample.appendInnerContent("  </context-param>");

        return webXmlCodeExample;
    }
}
