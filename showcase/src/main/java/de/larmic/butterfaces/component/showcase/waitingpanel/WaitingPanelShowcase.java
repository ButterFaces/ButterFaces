package de.larmic.butterfaces.component.showcase.waitingpanel;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
@SuppressWarnings("serial")
public class WaitingPanelShowcase extends AbstractCodeShowcase implements Serializable {

    private int delayInMillis = 500;

    public void doWaitingClick() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:waitingPanel id=\"waiting\"");
        xhtmlCodeExample.appendInnerContent("                        delay=\"" + this.delayInMillis + "\"");
        xhtmlCodeExample.appendInnerContent("                        rendered\"" + this.isRendered() + "\" />\n", true);

        xhtmlCodeExample.appendInnerContent("        <h:commandLink styleClass=\"btn btn-success\"");
        xhtmlCodeExample.appendInnerContent("                       action=\"#{myBean.waitForFiveSeconds}>\"");
        xhtmlCodeExample.appendInnerContent("            <!-- ajax tag is needed because waiting panel component");
        xhtmlCodeExample.appendInnerContent("                 s used ajax status to open and close overlay -->");
        xhtmlCodeExample.appendInnerContent("            <f:ajax />");
        xhtmlCodeExample.appendInnerContent("        </h:commandLink>");

        final JavaCodeExample javaCodeExample = new JavaCodeExample("waiting.demo", "MyBean", true);

        javaCodeExample.appendInnerContent("    public void waitForFiveSeconds() {\n");
        javaCodeExample.appendInnerContent("        try {\n");
        javaCodeExample.appendInnerContent("            Thread.sleep(5000);\n");
        javaCodeExample.appendInnerContent("        } catch (InterruptedException e) {\n");
        javaCodeExample.appendInnerContent("            // this error is not ok...\n");
        javaCodeExample.appendInnerContent("        }\n");
        javaCodeExample.appendInnerContent("    }\n\n");

        codeExamples.add(xhtmlCodeExample);
        codeExamples.add(javaCodeExample);
    }

    public int getDelayInMillis() {
        return delayInMillis;
    }

    public void setDelayInMillis(int delayInMillis) {
        this.delayInMillis = delayInMillis;
    }

}
