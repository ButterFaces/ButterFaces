package de.larmic.butterfaces.component.renderkit.html_basic.table.mojarra.mojarra;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by larmic on 12.05.16.
 */
public abstract class TableRenderer extends BaseTableRenderer {


    // ---------------------------------------------------------- Public Methods


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        if (!component.isRendered()) {
            return;
        }

        UIData data = (UIData) component;
        data.setRowIndex(-1);

        // Render the beginning of the table
        ResponseWriter writer = context.getResponseWriter();

        renderTableStart(context, component, writer);

        // Render the caption (if any)
        renderCaption(context, data, writer);

        // Render the header facets (if any)
        renderHeader(context, component, writer);
    }


    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        if (!component.isRendered()) {
            return;
        }

        UIData data = (UIData) component;

        ResponseWriter writer = context.getResponseWriter();

        // Check if any columns are being rendered, if not
        // render the minimal markup and exit
        TableMetaInfo info = getMetaInfo(context, data);
        if (info.columns.isEmpty()) {
            renderEmptyTableBody(writer, data);
            return;
        }
        // Iterate over the rows of data that are provided
        int processed = 0;
        int rowIndex = data.getFirst() - 1;
        int rows = data.getRows();
        renderTableBodyStart(context, component, writer);
        boolean renderedRow = false;
        while (true) {

            // Have we displayed the requested number of rows?
            if ((rows > 0) && (++processed > rows)) {
                break;
            }
            // Select the current row
            data.setRowIndex(++rowIndex);
            if (!data.isRowAvailable()) {
                break; // Scrolled past the last row
            }

            // Render the beginning of this row
            renderRowStart(context, component, writer);

            // Render the row content
            renderRow(context, component, null, writer);

            // Render the ending of this row
            renderRowEnd(context, component, writer);
            renderedRow = true;

        }

        // fill an empty tbody, if no row has been rendered
        if (!renderedRow) {
            this.renderEmptyTableRow(writer, data);
        }
        renderTableBodyEnd(context, component, writer);

        // Clean up after ourselves
        data.setRowIndex(-1);

    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        if (!component.isRendered()) {
            return;
        }

        clearMetaInfo(context, component);
        ((UIData) component).setRowIndex(-1);

        // Render the ending of this table
        renderTableEnd(context, component, context.getResponseWriter());

    }

    @Override
    public boolean getRendersChildren() {

        return true;

    }

    // ------------------------------------------------------- Private Methods

    private void renderEmptyTableBody(final ResponseWriter writer,
                                      final UIComponent component)
            throws IOException {

        writer.startElement("tbody", component);
        this.renderEmptyTableRow(writer, component);
        writer.endElement("tbody");

    }

    private void renderEmptyTableRow(final ResponseWriter writer,
                                     final UIComponent component)
            throws IOException {

        writer.startElement("tr", component);
        List<UIColumn> columns = getColumns(component);
        for (UIColumn column : columns) {
            if (column.isRendered()) {
                writer.startElement("td", component);
                writer.endElement("td");
            }
        }
        writer.endElement("tr");
    }

    /**
     * <p>Return an Iterator over the <code>UIColumn</code> children of the
     * specified <code>UIData</code> that have a <code>rendered</code> property
     * of <code>true</code>.</p>
     *
     * @param table the table from which to extract children
     * @return the List of all UIColumn children
     */
    private List<UIColumn> getColumns(UIComponent table) {
        int childCount = table.getChildCount();
        if (childCount > 0) {
            List<UIColumn> results =
                    new ArrayList<UIColumn>(childCount);
            for (UIComponent kid : table.getChildren()) {
                if ((kid instanceof UIColumn) && kid.isRendered()) {
                    results.add((UIColumn) kid);
                }
            }
            return results;
        } else {
            return Collections.emptyList();
        }
    }
}
