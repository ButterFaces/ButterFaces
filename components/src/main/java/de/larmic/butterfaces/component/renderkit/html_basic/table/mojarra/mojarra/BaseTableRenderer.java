package de.larmic.butterfaces.component.renderkit.html_basic.table.mojarra.mojarra;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Base class for concrete Grid and Table renderers.
 */
public abstract class BaseTableRenderer extends HtmlBasicRenderer {


    // ------------------------------------------------------- Protected Methods

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
                                         ResponseWriter writer)
            throws IOException;


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
                                      UIComponent table,
                                      UIComponent row,
                                      ResponseWriter writer)
            throws IOException;


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
    protected abstract void renderTableStart(FacesContext context, UIComponent table, ResponseWriter writer) throws IOException;


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
                                  ResponseWriter writer)
            throws IOException {

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
    protected void renderTableBodyStart(FacesContext context,
                                        UIComponent table,
                                        ResponseWriter writer)
            throws IOException {

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
                                      ResponseWriter writer)
            throws IOException {

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
                                  ResponseWriter writer)
            throws IOException {
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
                                ResponseWriter writer)
            throws IOException {

        writer.endElement("tr");
        writer.writeText("\n", table, null);

    }


    /**
     * Returns a <code>TableMetaInfo</code> object containing details such
     * as row and column classes, columns, and a mechanism for scrolling through
     * the row/column classes.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table that's being rendered
     * @return the <code>TableMetaInfo</code> for provided table
     */
    protected TableMetaInfo getMetaInfo(FacesContext context,
                                        UIComponent table) {

        String key = createKey(table);
        Map<Object, Object> attributes = context.getAttributes();
        TableMetaInfo info = (TableMetaInfo)
                attributes.get(key);
        if (info == null) {
            info = new TableMetaInfo(table);
            attributes.put(key, info);
        }
        return info;

    }


    /**
     * Removes the cached TableMetaInfo from the specified component.
     *
     * @param context the <code>FacesContext</code> for the current request
     * @param table   the table from which the TableMetaInfo will be removed
     */
    protected void clearMetaInfo(FacesContext context, UIComponent table) {

        context.getAttributes().remove(createKey(table));

    }


    /**
     * Creates a unique key based on the provided <code>UIComponent</code> with
     * which the TableMetaInfo can be looked up.
     *
     * @param table the table that's being rendered
     * @return a unique key to store the metadata in the request and still have
     * it associated with a specific component.
     */
    protected String createKey(UIComponent table) {

        return TableMetaInfo.KEY + '_' + table.hashCode();

    }


// ----------------------------------------------------------- Inner Classes


    public static class TableMetaInfo {

        public static final String KEY = de.larmic.butterfaces.component.renderkit.html_basic.table.mojarra.mojarra.BaseTableRenderer.TableMetaInfo.class.getName();

        public final List<UIColumn> columns;
        public final int columnCount;
        public int columnStyleCounter;


        // -------------------------------------------------------- Constructors


        public TableMetaInfo(UIComponent table) {
            columns = getColumns(table);
            columnCount = columns.size();
        }


        // ------------------------------------------------------ Public Methods


        /**
         * Reset the counter used to apply column styles.
         */
        public void newRow() {

            columnStyleCounter = 0;

        }


        // ----------------------------------------------------- Private Methods

        /**
         * <p>Return an Iterator over the <code>UIColumn</code> children of the
         * specified <code>UIData</code> that have a <code>rendered</code> property
         * of <code>true</code>.</p>
         *
         * @param table the table from which to extract children
         * @return the List of all UIColumn children
         */
        private static List<UIColumn> getColumns(UIComponent table) {

            if (table instanceof UIData) {
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
            } else {
                int count;
                Object value = table.getAttributes().get("columns");
                if ((value != null) && (value instanceof Integer)) {
                    count = ((Integer) value);
                } else {
                    count = 2;
                }
                if (count < 1) {
                    count = 1;
                }
                List<UIColumn> result = new ArrayList<UIColumn>(count);
                for (int i = 0; i < count; i++) {
                    result.add(new UIColumn());
                }
                return result;
            }

        }
    }
}
