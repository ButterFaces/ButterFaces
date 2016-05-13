package de.larmic.butterfaces.component.renderkit.html_basic.table.mojarra.mojarra;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

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

        // Iterate over the rows of data that are provided
        int processed = 0;
        int rowIndex = data.getFirst() - 1;
        int rows = data.getRows();
        renderTableBodyStart(context, component, writer);
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
}
