package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 10.09.14.
 */
@FacesRenderer(componentFamily = HtmlTable.COMPONENT_FAMILY, rendererType = HtmlTable.RENDERER_TYPE)
public class TableRenderer extends com.sun.faces.renderkit.html_basic.HtmlBasicRenderer {

    public static final String ELEMENT_TABLE = "table";
    public static final String ATTRIBUTE_CLASS = "class";
    public static final String ELEMENT_THEAD = "thead";
    public static final String ELEMENT_TR = "tr";
    public static final String ELEMENT_TH = "th";
    public static final String ELEMENT_TBODY = "tbody";

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final HtmlTable table = (HtmlTable) component;

        final ArrayList<HtmlColumn> columns = getColumns(table);

        if (table.isRendered() && !columns.isEmpty()) {
            writer.startElement(ELEMENT_TABLE, component);
            writer.writeAttribute("id", component.getClientId(), null);
            writer.writeAttribute(ATTRIBUTE_CLASS, "table table-striped table-hover", null);

            this.encodeHeader(writer, table);
            this.encodeBody(writer, table);
        }
    }

    @Override
    public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final HtmlTable table = (HtmlTable) component;

        if (table.isRendered()) {
            writer.endElement(ELEMENT_TABLE);
        }
    }

    private void encodeHeader(final ResponseWriter writer, final HtmlTable table) throws IOException {
        final ArrayList<HtmlColumn> columns = getColumns(table);

        writer.startElement(ELEMENT_THEAD, table);
        writer.startElement(ELEMENT_TR, table);
        for (HtmlColumn column : columns) {
            writer.startElement(ELEMENT_TH, table);
            writer.writeAttribute("id", column.getClientId(), null);
            writer.writeText(column.getLabel(), null);
            writer.endElement(ELEMENT_TH);
        }
        writer.endElement(ELEMENT_TR);
        writer.endElement(ELEMENT_THEAD);
    }

    private void encodeBody(final ResponseWriter writer, final HtmlTable table) throws IOException {
        writer.startElement(ELEMENT_TBODY, table);
        writer.startElement(ELEMENT_TR, table);
        writer.endElement(ELEMENT_TR);
        writer.endElement(ELEMENT_TBODY);
    }

    private ArrayList<HtmlColumn> getColumns(final HtmlTable table) {
        final ArrayList<HtmlColumn> columns = new ArrayList<>();
        final List<UIComponent> tableChildren = table.getChildren();
        for (UIComponent tableChild : tableChildren) {
            if (tableChild instanceof HtmlColumn) {
                columns.add((HtmlColumn) tableChild);
            }
        }
        return columns;
    }
}
