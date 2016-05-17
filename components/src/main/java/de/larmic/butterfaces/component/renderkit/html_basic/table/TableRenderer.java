package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.base.renderer.HtmlBasicRenderer;
import de.larmic.butterfaces.component.behavior.JsfAjaxRequest;
import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.table.cache.TableColumnCache;
import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.model.table.SortType;
import de.larmic.butterfaces.resolver.ClientBehaviorResolver;
import de.larmic.butterfaces.resolver.WebXmlParameters;
import de.larmic.butterfaces.util.StringJoiner;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by larmic on 10.09.14.
 */
@FacesRenderer(componentFamily = HtmlTable.COMPONENT_FAMILY, rendererType = HtmlTable.RENDERER_TYPE)
public class TableRenderer extends BaseTableRenderer {

    private boolean hasColumnWidthSet;
    // dirty: method renderRowStart does not have a rowIndex parameter but it should.
    // alternative: copy encode children but this means coping a lot of private methods... :(
    // we will try this way... maybe migrating later...
    private int rowIndex;
    private boolean foundSelectedRow;

    private WebXmlParameters webXmlParameters;

    @Override
    public void encodeBegin(final FacesContext context,
                            final UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        final HtmlTable table = (HtmlTable) component;
        final TableColumnCache tableColumnCache = table.getTableColumnCache(context);
        this.hasColumnWidthSet = hasColumnWidthSet(tableColumnCache.getCachedColumns());
        this.rowIndex = 0;
        this.webXmlParameters = new WebXmlParameters(context.getExternalContext());
        this.foundSelectedRow = false;

        super.encodeBegin(context, component);
    }

    @Override
    protected void renderHeader(final FacesContext context,
                                final UIComponent table,
                                final ResponseWriter writer) throws IOException {
        final HtmlTable htmlTable = (HtmlTable) table;
        final TableColumnCache tableColumnCache = htmlTable.getTableColumnCache(context);

        if (hasColumnWidthSet) {

            writer.startElement("colgroup", table);

            int columnNumber = 0;

            for (HtmlColumn column : tableColumnCache.getCachedColumns()) {
                writer.startElement("col", table);
                writer.writeAttribute("class", "butter-table-colgroup", null);
                writer.writeAttribute("columnNumber", "" + columnNumber, null);

                final StringBuilder style = new StringBuilder();

                if (StringUtils.isNotEmpty(column.getColWidth())) {
                    style.append("width: ");
                    style.append(column.getColWidth());
                }
                if (this.isHideColumn(htmlTable, column)) {
                    if (style.length() > 0) {
                        style.append("; ");
                    }
                    style.append("display: none");
                }

                if (style.length() > 0) {
                    writer.writeAttribute("style", style.toString(), null);
                }
                writer.endElement("col");

                columnNumber++;
            }
            writer.endElement("colgroup");
        }

        writer.startElement("thead", table);
        writer.startElement("tr", table);

        int columnNumber = 0;

        for (HtmlColumn column : tableColumnCache.getCachedColumns()) {
            column.setWebXmlParameters(webXmlParameters);
            column.setColumnNumberUsedByTable(columnNumber);
            column.encodeBegin(context);
            column.encodeEnd(context);
            columnNumber++;
        }
        writer.endElement("tr");
        writer.endElement("thead");
    }

    @Override
    protected void renderTableStart(final FacesContext context,
                                    final UIComponent component,
                                    final ResponseWriter writer) throws IOException {
        final HtmlTable table = (HtmlTable) component;

        writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, table);
        writer.writeAttribute("id", component.getClientId(context), "id");
        writer.writeAttribute("class", StringJoiner.on(' ').join("butter-table").join(StringUtils.getNullSafeValue(table.getStyleClass())).toString(), null);
        if (StringUtils.isNotEmpty(table.getStyle())) {
            writer.writeAttribute("style", table.getStyle(), null);
        }

        writer.startElement(HtmlBasicRenderer.ELEMENT_DIV, table);

        writer.writeAttribute("class", "table-responsive", "styleClass");

        writer.startElement("table", table);
        final StringBuilder tableStyleClass = new StringBuilder("table table-hover");

        if (this.hasColumnWidthSet) {
            tableStyleClass.append(" table-fixed");
        }
        if (table.isTableCondensed()) {
            tableStyleClass.append(" table-condensed");
        }
        if (table.isTableBordered()) {
            tableStyleClass.append(" table-bordered");
        }
        if (table.isTableStriped()) {
            tableStyleClass.append(" table-striped");
        }

        writer.writeAttribute("class", tableStyleClass.toString(), "styleClass");

        writer.writeText("\n", table, null);
    }

    @Override
    protected void renderTableEnd(final FacesContext context,
                                  final UIComponent component,
                                  final ResponseWriter writer) throws IOException {
        super.renderTableEnd(context, component, writer);
        writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
        writer.endElement(HtmlBasicRenderer.ELEMENT_DIV);
    }

    @Override
    protected void renderRowStart(final FacesContext context,
                                  final UIComponent component,
                                  final ResponseWriter writer) throws IOException {
        final HtmlTable htmlTable = (HtmlTable) component;

        final String clientId = htmlTable.getClientId();
        final String baseClientId = clientId.substring(0, clientId.length() - (rowIndex + "").length() - 1);

        writer.startElement("tr", htmlTable);
        writer.writeAttribute("rowIndex", rowIndex, null);
        final String rowClass = StringUtils.isNotEmpty(htmlTable.getRowClass()) ? "butter-table-row " + htmlTable.getRowClass() : "butter-table-row";

        if (!foundSelectedRow && this.isRowSelected(htmlTable, rowIndex)) {
            writer.writeAttribute("class", rowClass + " butter-table-row-selected", null);
            foundSelectedRow = true;
        } else {
            writer.writeAttribute("class", rowClass, null);
        }

        final AjaxBehavior clickAjaxBehavior = ClientBehaviorResolver.resolveActiveAjaxBehavior(htmlTable, "click");

        if (clickAjaxBehavior != null && htmlTable.getSingleSelectionListener() != null) {
            final JsfAjaxRequest ajaxRequest = new JsfAjaxRequest(baseClientId, true)
                    .setEvent("click_" + rowIndex)
                    .setRender(htmlTable, "click")
                    .setBehaviorEvent("click_" + rowIndex);
            final String jQueryPluginCall = RenderUtils.createJQueryPluginCall(htmlTable.getClientId(), "selectTableRow({rowIndex:'" + rowIndex + "'})");
            writer.writeAttribute("onclick", ajaxRequest.toString() + ";" + jQueryPluginCall.replaceFirst(clientId, baseClientId), null);
        }

        writer.writeText("\n", htmlTable, null);

        rowIndex++;
    }

    protected void renderRow(FacesContext context,
                             HtmlTable table,
                             UIComponent child,
                             ResponseWriter writer) throws IOException {

        // Iterate over the child UIColumn components for each row
        final TableColumnCache info = table.getTableColumnCache(context);

        int columnNumber = 0;

        for (HtmlColumn column : info.getCachedColumns()) {
            if (table.isHideColumn(column)) {
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
                if (StringUtils.isNotEmpty(column.getStyleClass())) {
                    sb.append(" ").append(column.getStyleClass());
                }
                writer.writeAttribute("class", sb.toString(), null);
                writer.writeAttribute("columnNumber", "" + columnNumber, null);

                //********************************************** hide column if models says that
                if (this.isHideColumn(table, column)) {
                    writer.writeAttribute("style", "display:none", null);
                } else {
                    if (StringUtils.isNotEmpty(column.getStyle())) {
                        writer.writeAttribute("style", column.getStyle(), null);
                    }
                }
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

    private boolean isRowSelected(final HtmlTable table, final int rowIndex) {
        if (table.getSingleSelectionListener() != null) {
            final Object rowObject = findRowObject(table, rowIndex);
            return table.getSingleSelectionListener().isValueSelected(rowObject);
        }

        return false;
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final HtmlTable table = (HtmlTable) component;
        final Map<String, List<ClientBehavior>> behaviors = table.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return;
        }

        final Object untypedTableValue = table.getValue();


        if (!(untypedTableValue instanceof Iterable)) {
            return;
        }

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (behaviorEvent != null && behaviorEvent.contains("_")) {
            final String[] split = behaviorEvent.split("_");
            final String event = split[0];
            try {
                final int eventNumber = Integer.valueOf(split[1]);
                if ("click".equals(event)) {
                    final Object rowObject = findRowObject(table, eventNumber);

                    if (rowObject != null) {
                        final TableSingleSelectionListener listener = table.getSingleSelectionListener();

                        if (listener != null) {
                            listener.processTableSelection(rowObject);
                        }

                    }
                } else if ("sort".equals(event) && table.getModel() != null) {
                    final TableColumnCache tableColumnCache = table.getTableColumnCache(context);
                    final HtmlColumn sortedColumn = tableColumnCache.getCachedColumns().get(eventNumber);
                    final String tableUniqueIdentifier = table.getModelUniqueIdentifier();
                    final String columnUniqueIdentifier = sortedColumn.getModelUniqueIdentifier();
                    if (table.getTableSortModel().getSortType(tableUniqueIdentifier, columnUniqueIdentifier) == SortType.ASCENDING) {
                        table.getTableSortModel().sortColumn(tableUniqueIdentifier, columnUniqueIdentifier, sortedColumn.getSortBy(), SortType.DESCENDING);
                    } else {
                        table.getTableSortModel().sortColumn(tableUniqueIdentifier, columnUniqueIdentifier, sortedColumn.getSortBy(), SortType.ASCENDING);
                    }
                }
            } catch (NumberFormatException e) {
                // event is not fired by table component
            }
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

    private Object findRowObject(final HtmlTable table, final int row) {
        final Object value = table.getValue();

        if (value instanceof Iterable) {
            final Iterator iterator = ((Iterable) value).iterator();
            int actualRow = 0;

            while (iterator.hasNext()) {
                final Object value1 = iterator.next();

                if (actualRow == row) {
                    return value1;
                }

                actualRow++;
            }

            return null;
        }

        return null;
    }

    private boolean hasColumnWidthSet(final List<HtmlColumn> columns) {
        for (HtmlColumn column : columns) {
            if (StringUtils.isNotEmpty(column.getColWidth())) {
                return true;
            }
        }

        return false;
    }
}
