package de.larmic.butterfaces.component.showcase.waitingpanel;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class WaitingPanelShowcase extends AbstractCodeShowcase implements Serializable {

    private int delayInMillis = 500;
    private boolean blockpage = true;

    public void doShortWaitingClick() {
        try {
            Thread.sleep(550);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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
        xhtmlCodeExample.appendInnerContent("                        delay=\"" + delayInMillis + "\"");
        xhtmlCodeExample.appendInnerContent("                        blockpage=\"" + blockpage + "\"");
        xhtmlCodeExample.appendInnerContent("                        rendered\"" + this.isRendered() + "\" />\n", true);

        xhtmlCodeExample.appendInnerContent("        <h:commandLink styleClass=\"btn btn-success\"");
        xhtmlCodeExample.appendInnerContent("                       action=\"#{myBean.waitForFiveSeconds}>\"");
        xhtmlCodeExample.appendInnerContent("            <!-- ajax tag is needed because waiting panel component");
        xhtmlCodeExample.appendInnerContent("                 is used ajax status to open and close overlay -->");
        xhtmlCodeExample.appendInnerContent("            <f:ajax />");
        xhtmlCodeExample.appendInnerContent("        </h:commandLink>");

        final JavaCodeExample javaCodeExample = new JavaCodeExample("MyBean.java", "mybean", "waiting.demo", "MyBean", true);

        javaCodeExample.appendInnerContent("    public void waitForFiveSeconds() {");
        javaCodeExample.appendInnerContent("        try {");
        javaCodeExample.appendInnerContent("            Thread.sleep(5000);");
        javaCodeExample.appendInnerContent("        } catch (InterruptedException e) {");
        javaCodeExample.appendInnerContent("            // this error is not ok...");
        javaCodeExample.appendInnerContent("        }");
        javaCodeExample.appendInnerContent("    }");

        codeExamples.add(xhtmlCodeExample);
        codeExamples.add(javaCodeExample);
    }

    public int getDelayInMillis() {
        return delayInMillis;
    }

    public void setDelayInMillis(int delayInMillis) {
        this.delayInMillis = delayInMillis;
    }

    public boolean isBlockpage() {
        return blockpage;
    }

    public void setBlockpage(boolean blockpage) {
        this.blockpage = blockpage;
    }
}
