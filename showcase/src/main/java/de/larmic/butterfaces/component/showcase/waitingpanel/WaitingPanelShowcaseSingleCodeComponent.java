package de.larmic.butterfaces.component.showcase.waitingpanel;

import de.larmic.butterfaces.component.renderkit.html_basic.ajax.WaitingPanelRenderer;
import de.larmic.butterfaces.component.showcase.AbstractShowcaseSingleCodeComponent;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
@SuppressWarnings("serial")
public class WaitingPanelShowcaseSingleCodeComponent extends AbstractShowcaseSingleCodeComponent implements Serializable {

    private WaitingPanelChildrenType waitingPanelChildrenType = WaitingPanelChildrenType.NONE;
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

        sb.append("        <b:waitingPanel id=\"waiting\"\n");

        if (this.delayInMillis != WaitingPanelRenderer.DEFAULT_WAITING_PANEL_DELAY) {
            this.appendString(" delay", this.delayInMillis + "", sb);
        }
        if (waitingPanelChildrenType == WaitingPanelChildrenType.EXAMPLE_1) {
            this.appendString(" styleClass", "bigWaitingContent", sb);
        } else if (waitingPanelChildrenType == WaitingPanelChildrenType.EXAMPLE_2) {
            this.appendString(" styleClass", "smallWaitingContent", sb);
        }
        this.appendBoolean(" rendered", this.isRendered(), sb, true);

        if (waitingPanelChildrenType == WaitingPanelChildrenType.EXAMPLE_1) {
            sb.append("            Big waiting example\n");
        } else if (waitingPanelChildrenType == WaitingPanelChildrenType.EXAMPLE_2) {
            sb.append("           Small waiting example\n");
        }
        sb.append("        </b:waitingPanel>\n\n");

        sb.append("        <h:commandLink styleClass=\"btn btn-success\"\n");
        this.appendString("action", "#{myBean.waitForFiveSeconds}>", sb);
        sb.append("            <f:ajax />\n");
        sb.append("        </h:commandLink>");

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected void addCss(final StringBuilder sb) {
        if (waitingPanelChildrenType == WaitingPanelChildrenType.EXAMPLE_1) {
            sb.append(".bigWaitingContent .butter-component-waitingPanel-body {\n");
            sb.append("    height: 150px;\n");
            sb.append("    font-size: 25px;\n");
            sb.append("    background-color: lightcoral;\n");
            sb.append("}");
        } else if (waitingPanelChildrenType == WaitingPanelChildrenType.EXAMPLE_2) {
            sb.append(".smallWaitingContent .butter-component-waitingPanel-body {\n");
            sb.append("    height: 30px;\n");
            sb.append("    font-size: 10px;\n");
            sb.append("    top: 50%;\n");
            sb.append("    padding: 8px;\n");
            sb.append("}");
        }

    }

    public List<SelectItem> getChildrenTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final WaitingPanelChildrenType type : WaitingPanelChildrenType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                       ";
    }

    public int getDelayInMillis() {
        return delayInMillis;
    }

    public void setDelayInMillis(int delayInMillis) {
        this.delayInMillis = delayInMillis;
    }

    public WaitingPanelChildrenType getChildrenType() {
        return waitingPanelChildrenType;
    }

    public void setChildrenType(WaitingPanelChildrenType childrenType) {
        this.waitingPanelChildrenType = childrenType;
    }

    public boolean isRenderChildrenExample1() {
        return this.waitingPanelChildrenType == WaitingPanelChildrenType.EXAMPLE_1;
    }

    public boolean isRenderChildrenExample2() {
        return this.waitingPanelChildrenType == WaitingPanelChildrenType.EXAMPLE_2;
    }

    public boolean isRenderNoChildren() {
        return this.waitingPanelChildrenType == WaitingPanelChildrenType.NONE;
    }
}
