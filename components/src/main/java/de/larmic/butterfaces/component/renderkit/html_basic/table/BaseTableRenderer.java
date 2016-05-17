package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.table.HtmlTable;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Base class for concrete Grid and Table renderers.
 */
public abstract class BaseTableRenderer extends HtmlBasicRenderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final UIData data = (UIData) component;
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

        final UIData data = (UIData) component;
        final ResponseWriter writer = context.getResponseWriter();

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
            renderRow(context, (HtmlTable) component, null, writer);

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

        ((HtmlTable) component).clearMetaInfo(context, component);
        ((UIData) component).setRowIndex(-1);

        // Render the ending of this table
        renderTableEnd(context, component, context.getResponseWriter());

    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    /**
     * Called to render the opening/closing <code>thead</code> elements
     * and any content nested between.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table that's being rendered
     * @param writer  the current writer
     * @throws IOException if content cannot be written
     */
    protected abstract void renderHeader(FacesContext context,
                                         UIComponent table,
                                         ResponseWriter writer) throws IOException;


    /**
     * Call to render the content that should be included between opening
     * and closing <code>tr</code> elements.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table that's being rendered
     * @param row     the current row (if any - an implmenetation may not need this)
     * @param writer  the current writer
     * @throws IOException if content cannot be written
     */
    protected abstract void renderRow(FacesContext context,
                                      HtmlTable table,
                                      UIComponent row,
                                      ResponseWriter writer) throws IOException;


    /**
     * Renders the start of a table and applies the value of
     * <code>styleClass</code> if available and renders any
     * pass through attributes that may be specified.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table that's being rendered
     * @param writer  the current writer
     *                supports
     * @throws IOException if content cannot be written
     */
    protected abstract void renderTableStart(FacesContext context,
                                             UIComponent table,
                                             ResponseWriter writer) throws IOException;


    /**
     * Renders the closing <code>table</code> element.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table that's being rendered
     * @param writer  the current writer
     * @throws IOException if content cannot be written
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected void renderTableEnd(FacesContext context,
                                  UIComponent table,
                                  ResponseWriter writer) throws IOException {
        writer.endElement("table");
        writer.writeText("\n", table, null);
    }

    /**
     * Renders the starting <code>tbody</code> element.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table that's being rendered
     * @param writer  the current writer
     * @throws IOException if content cannot be written
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private void renderTableBodyStart(FacesContext context,
                                      UIComponent table,
                                      ResponseWriter writer) throws IOException {
        writer.startElement("tbody", table);
        writer.writeText("\n", table, null);
    }


    /**
     * Renders the closing <code>tbody</code> element.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table that's being rendered
     * @param writer  the current writer
     * @throws IOException if content cannot be written
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected void renderTableBodyEnd(FacesContext context,
                                      UIComponent table,
                                      ResponseWriter writer) throws IOException {
        writer.endElement("tbody");
        writer.writeText("\n", table, null);
    }


    /**
     * Renders the starting <code>tr</code> element applying any values
     * from the <code>rowClasses</code> attribute.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table that's being rendered
     * @param writer  the current writer
     * @throws IOException if content cannot be written
     */
    protected void renderRowStart(FacesContext context,
                                  UIComponent table,
                                  ResponseWriter writer) throws IOException {
        writer.startElement("tr", table);
        writer.writeText("\n", table, null);
    }


    /**
     * Renders the closing <code>rt</code> element.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table that's being rendered
     * @param writer  the current writer
     * @throws IOException if content cannot be written
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected void renderRowEnd(FacesContext context,
                                UIComponent table,
                                ResponseWriter writer) throws IOException {
        writer.endElement("tr");
        writer.writeText("\n", table, null);
    }
}
