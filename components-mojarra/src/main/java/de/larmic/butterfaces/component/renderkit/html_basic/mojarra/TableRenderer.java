package de.larmic.butterfaces.component.renderkit.html_basic.mojarra;


import com.sun.faces.renderkit.html_basic.BaseTableRenderer;
import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

public class TableRenderer extends com.sun.faces.renderkit.html_basic.TableRenderer {

    protected void renderRow(FacesContext context,
                             UIComponent table,
                             UIComponent child,
                             ResponseWriter writer) throws IOException {

        // Iterate over the child UIColumn components for each row
        BaseTableRenderer.TableMetaInfo info = getMetaInfo(context, table);
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
                //********************************************** ADD butter style class
                writer.writeAttribute("class", "butter-component-table-column", null);
                writer.writeAttribute("columnNumber", "" + columnNumber, null);

                //********************************************** hide column if models says that
                if (column instanceof HtmlColumn && table instanceof HtmlTable && this.isHideColumn((HtmlTable) table, (HtmlColumn) column)) {
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
        if (table.getTableColumnVisibilityModel() != null) {
            final String tableUniqueIdentifier = table.getModelUniqueIdentifier();
            final String columnUniqueIdentifier = column.getModelUniqueIdentifier();
            final Boolean hideColumn = table.getTableColumnVisibilityModel().isColumnHidden(tableUniqueIdentifier, columnUniqueIdentifier);
            if (hideColumn != null) {
                return hideColumn;
            }
        }
        return column.isHideColumn();
    }

}
