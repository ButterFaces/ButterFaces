package de.larmic.butterfaces.component.renderkit.html_basic.table.mojarra;

import de.larmic.butterfaces.component.html.table.HtmlColumnNew;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.html.table.HtmlTableNew;
import de.larmic.butterfaces.component.renderkit.html_basic.table.mojarra.mojarra.BaseTableRenderer;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

public class MojarraTableRenderer extends de.larmic.butterfaces.component.renderkit.html_basic.table.mojarra.mojarra.TableRenderer {

    protected void renderRow(FacesContext context,
                             UIComponent table,
                             UIComponent child,
                             ResponseWriter writer) throws IOException {

        // Iterate over the child UIColumn components for each row
        BaseTableRenderer.TableMetaInfo info = getMetaInfo(context, table);
        info.newRow();

        int columnNumber = 0;

        for (UIColumn column : info.columns) {

            if (column instanceof HtmlColumnNew && ((HtmlColumnNew) column).isHideColumn()) {
                continue;
            }

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
                StringBuilder sb = new StringBuilder("butter-component-table-column");
                if (column instanceof HtmlColumnNew) {
                    HtmlColumnNew htmlColumn = (HtmlColumnNew) column;
                    if (StringUtils.isNotEmpty(htmlColumn.getStyleClass())) {
                        sb.append(" ").append(htmlColumn.getStyleClass());
                    }
                }
                writer.writeAttribute("class", sb.toString(), null);
                writer.writeAttribute("columnNumber", "" + columnNumber, null);

                //********************************************** hide column if models says that
                if (column instanceof HtmlColumnNew && table instanceof HtmlTable && this.isHideColumn((HtmlTableNew) table, (HtmlColumnNew) column)) {
                    writer.writeAttribute("style", "display:none", null);
                } else if (column instanceof HtmlColumnNew) {
                    HtmlColumnNew htmlColumn = (HtmlColumnNew) column;
                    if (StringUtils.isNotEmpty(htmlColumn.getStyle())) {
                        writer.writeAttribute("style", htmlColumn.getStyle(), null);
                    }
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

    private boolean isHideColumn(final HtmlTableNew table, final HtmlColumnNew column) {
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