package de.larmic.butterfaces.component.showcase;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
@SuppressWarnings("serial")
public class WaitingPanelShowcaseComponent extends AbstractShowcaseComponent implements Serializable {

    private int delayInMillis = 500;

    public void doWaitingClick() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void addJavaCode(final StringBuilder sb) {
        sb.append("package de.larmic.ajax.waiting,demo;\n\n");

        sb.append("import javax.faces.view.ViewScoped;\n");
        sb.append("import javax.inject.Named;\n\n");

        sb.append("@ViewScoped\n");
        sb.append("@Named\n");
        sb.append("public class MyBean implements Serializable {\n\n");
        sb.append("    public void waitForFiveSeconds() {\n");
        sb.append("        try {\n");
        sb.append("            Thread.sleep(5000);\n");
        sb.append("        } catch (InterruptedException e) {\n");
        sb.append("            // this error is not ok...\n");
        sb.append("        }\n");
        sb.append("    }\n\n");
        sb.append("}");
    }

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

        sb.append("        <b:waitingPanel id=\"input\"\n");

        this.appendString("delay", this.delayInMillis + "", sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        sb.append("        </b:waitingPanel>");

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    public int getDelayInMillis() {
        return delayInMillis;
    }

    public void setDelayInMillis(int delayInMillis) {
        this.delayInMillis = delayInMillis;
    }
}
