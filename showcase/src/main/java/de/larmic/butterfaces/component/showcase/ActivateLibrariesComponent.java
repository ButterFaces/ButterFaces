package de.larmic.butterfaces.component.showcase;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class ActivateLibrariesComponent extends AbstractShowcaseComponent implements Serializable {

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

        sb.append("        <l:activateLibraries id=\"input\"\n");
        this.appendBoolean("rendered", this.isRendered(), sb, true);
        sb.append("        </l:activateLibraries>");

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                             ";
    }
}
