/*
 * Copied from Mojarra. Add some row and column numbers.
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package de.larmic.butterfaces.component.renderkit.html_basic.mojarra;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.html_basic.BaseTableRenderer;
import com.sun.faces.util.Util;
import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.*;

/**
 * <p>Render a {@link javax.faces.component.UIData} component as a two-dimensional table.</p>
 */

public class TableRenderer extends BaseTableRenderer {


    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.DATATABLE);

    // ---------------------------------------------------------- Public Methods


    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        UIData data = (UIData) component;
        data.setRowIndex(-1);

        // Render the beginning of the table
        ResponseWriter writer = context.getResponseWriter();

        renderTableStart(context, component, writer, ATTRIBUTES);

        // Render the caption (if any)
        renderCaption(context, data, writer);

        // Render column groups (if any)
        renderColumnGroups(context, data);

        // Render the header facets (if any)
        renderHeader(context, component, writer);

        // Render the footer facets (if any)
        renderFooter(context, component, writer);

    }


    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {

        rendererParamsNotNull(context, component);

        if (!shouldEncodeChildren(component)) {
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
        List<Integer> bodyRows = getBodyRows(context.getExternalContext().getApplicationMap(), data);
        boolean hasBodyRows = (bodyRows != null && !bodyRows.isEmpty());
        boolean wroteTableBody = false;
        if (!hasBodyRows) {
            renderTableBodyStart(context, component, writer);
        }
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

            // render any table body rows
            if (hasBodyRows && bodyRows.contains(data.getRowIndex())) {
                if (wroteTableBody) {
                    writer.endElement("tbody");
                }
                writer.startElement("tbody", data);
                wroteTableBody = true;
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

        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
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


    // ------------------------------------------------------- Protected Methods


    private List<Integer> getBodyRows(Map<String, Object> appMap, UIData data) {

        List<Integer> result = null;
        String bodyRows = (String) data.getAttributes().get("bodyrows");
        if (bodyRows != null) {
            String[] rows = Util.split(appMap, bodyRows, ",");
            if (rows != null) {
                result = new ArrayList<Integer>(rows.length);
                for (String curRow : rows) {
                    result.add(Integer.valueOf(curRow));
                }
            }
        }

        return result;

    }


    protected void renderColumnGroups(FacesContext context,
                                      UIComponent table)
            throws IOException {

        UIComponent colGroups = getFacet(table, "colgroups");
        if (colGroups != null) {
            encodeRecursive(context, colGroups);
        }

    }

    protected void renderFooter(FacesContext context,
                                UIComponent table,
                                ResponseWriter writer)
            throws IOException {

        TableMetaInfo info = getMetaInfo(context, table);
        UIComponent footer = getFacet(table, "footer");
        // check if any footer has to be rendered
        if (footer == null && !info.hasFooterFacets) {
            return;
        }
        String footerClass = (String) table.getAttributes().get("footerClass");
        writer.startElement("tfoot", table);
        writer.writeText("\n", table, null);
        if (info.hasFooterFacets) {
            writer.startElement("tr", table);
            writer.writeText("\n", table, null);
            for (UIColumn column : info.columns) {
                String columnFooterClass =
                        (String) column.getAttributes().get("footerClass");
                writer.startElement("td", column);
                if (columnFooterClass != null) {
                    writer.writeAttribute("class", columnFooterClass,
                            "columnFooterClass");
                } else if (footerClass != null) {
                    writer.writeAttribute("class", footerClass, "footerClass");
                }
                UIComponent facet = getFacet(column, "footer");
                if (facet != null) {
                    writer.writeText("", table, null);
                    encodeRecursive(context, facet);
                }
                writer.endElement("td");
                writer.writeText("\n", table, null);
            }
            renderRowEnd(context, table, writer);
        }
        if (footer != null) {
            writer.startElement("tr", footer);
            writer.startElement("td", footer);
            if (footerClass != null) {
                writer.writeAttribute("class", footerClass, "footerClass");
            }
            if (info.columns.size() > 1) {
                writer.writeAttribute("colspan", String.valueOf(info.columns.size()), null);
            }
            encodeRecursive(context, footer);
            writer.endElement("td");
            renderRowEnd(context, table, writer);
        }
        writer.endElement("tfoot");
        writer.writeText("\n", table, null);

    }

    protected void renderHeader(FacesContext context,
                                UIComponent table,
                                ResponseWriter writer)
            throws IOException {

        TableMetaInfo info = getMetaInfo(context, table);
        UIComponent header = getFacet(table, "header");
        // check if any header has to be rendered
        if (header == null && !info.hasHeaderFacets) {
            return;
        }
        String headerClass = (String) table.getAttributes().get("headerClass");
        writer.startElement("thead", table);
        writer.writeText("\n", table, null);
        if (header != null) {
            writer.startElement("tr", header);
            writer.startElement("th", header);
            if (headerClass != null) {
                writer.writeAttribute("class", headerClass, "headerClass");
            }
            if (info.columns.size() > 1) {
                writer.writeAttribute("colspan", String.valueOf(info.columns.size()), null);
            }
            writer.writeAttribute("scope", "colgroup", null);
            encodeRecursive(context, header);
            writer.endElement("th");
            renderRowEnd(context, table, writer);
        }
        if (info.hasHeaderFacets) {
            writer.startElement("tr", table);
            writer.writeText("\n", table, null);
            for (UIColumn column : info.columns) {
                String columnHeaderClass =
                        (String) column.getAttributes().get("headerClass");
                writer.startElement("th", column);
                if (columnHeaderClass != null) {
                    writer.writeAttribute("class", columnHeaderClass,
                            "columnHeaderClass");
                } else if (headerClass != null) {
                    writer.writeAttribute("class", headerClass, "headerClass");
                }
                writer.writeAttribute("scope", "col", null);
                UIComponent facet = getFacet(column, "header");
                if (facet != null) {
                    encodeRecursive(context, facet);
                }
                writer.endElement("th");
                writer.writeText("\n", table, null);
            }
            renderRowEnd(context, table, writer);
        }
        writer.endElement("thead");
        writer.writeText("\n", table, null);

    }

    protected void renderRow(FacesContext context,
                             UIComponent table,
                             UIComponent child,
                             ResponseWriter writer) throws IOException {

        // Iterate over the child UIColumn components for each row
        TableMetaInfo info = getMetaInfo(context, table);
        info.newRow();

        int columnNumber = 0;

        for (UIColumn column : info.columns) {

            // Render the beginning of this cell
            boolean isRowHeader = false;
            Object rowHeaderValue = column.getAttributes().get("rowHeader");
            if (null != rowHeaderValue) {
                isRowHeader = Boolean.valueOf(rowHeaderValue.toString());
            }
            if (isRowHeader) {
                writer.startElement("th", column);
                writer.writeAttribute("scope", "row", null);
            } else {
                writer.startElement("td", column);
                writer.writeAttribute("class", "butter-component-table-column", null);
                writer.writeAttribute("columnNumber", "" + columnNumber, null);

                if (column instanceof HtmlColumn && table instanceof HtmlTable && this.isHideColumn((HtmlTable) table, (HtmlColumn)column)) {
                    writer.writeAttribute("style", "display:none", null);
                }
            }

            String columnClass = info.getCurrentColumnClass();
            if (columnClass != null) {
                writer.writeAttribute("class",
                        columnClass,
                        "columnClasses");
            }

            // Render the contents of this cell by iterating over
            // the kids of our kids
            for (Iterator<UIComponent> gkids = getChildren(column);
                 gkids.hasNext(); ) {
                encodeRecursive(context, gkids.next());
            }

            // Render the ending of this cell
            if (isRowHeader) {
                writer.endElement("th");
            } else {
                writer.endElement("td");
            }
            writer.writeText("\n", table, null);

            columnNumber++;
        }

    }

    private boolean isHideColumn(final HtmlTable table, final HtmlColumn column) {
        if (table.getTableColumnDisplayModel() != null) {
            final String tableUniqueIdentifier = StringUtils.getNotNullValue(table.getUniqueIdentifier(), table.getId());
            final String columnUniqueIdentifier = StringUtils.getNotNullValue(column.getUniqueIdentifier(), column.getId());
            final Boolean hideColumn = table.getTableColumnDisplayModel().isColumnHidden(tableUniqueIdentifier, columnUniqueIdentifier);
            if (hideColumn != null) {
                return hideColumn;
            }
        }
        return column.isHideColumn();
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
