package de.larmic.butterfaces.component.showcase;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class ActivateLibrariesSingleCodeComponent extends AbstractShowcaseSingleCodeComponent implements Serializable {

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

        sb.append("        <b:activateLibraries id=\"input\"\n");
        this.appendBoolean("rendered", this.isRendered(), sb, true);
        sb.append("        </b:activateLibraries>");

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                             ";
    }
}
