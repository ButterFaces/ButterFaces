/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic.table;

import de.larmic.butterfaces.component.behavior.JsfAjaxRequest;
import de.larmic.butterfaces.component.html.HtmlTooltip;
import de.larmic.butterfaces.component.html.repeat.visitor.DataVisitResult;
import de.larmic.butterfaces.component.html.repeat.visitor.DataVisitor;
import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
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
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Lars Michaelis
 */
@FacesRenderer(componentFamily = HtmlTable.COMPONENT_FAMILY, rendererType = HtmlTable.RENDERER_TYPE)
public class TableRenderer extends Renderer {

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        super.encodeBegin(context, component);

        final HtmlTable table = (HtmlTable) component;
        final ResponseWriter writer = context.getResponseWriter();
        final List<HtmlColumn> columns = table.getCachedColumns();

        if (!columns.isEmpty()) {
            writer.startElement("div", table);
            writer.writeAttribute("id", component.getClientId(context), "id");
            writer.writeAttribute("class", StringJoiner.on(' ').join("butter-table").join(StringUtils.getNullSafeValue(table.getStyleClass())).toString(), null);
            if (StringUtils.isNotEmpty(table.getStyle())) {
                writer.writeAttribute("style", table.getStyle(), null);
            }

            writer.startElement("table", table);
            writer.writeAttribute("class", createBootstrapTableStyleClasses(table), "styleclass");

            if (hasColumnWidthSet(table.getCachedColumns())) {
                renderHeaderColGroup(table, writer);
            }

            writer.startElement("thead", table);
            writer.startElement("tr", table);
            final Iterator<HtmlColumn> columnIterator = columns.iterator();
            int columnNumber = 0;
            while (columnIterator.hasNext()) {
                encodeColumnHeader(table, columnNumber, columnIterator.next(), context);
                columnNumber++;
            }
            writer.endElement("tr");
            writer.endElement("thead");
            writer.startElement("tbody", table);
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!component.isRendered()) {
            return;
        }

        super.encodeEnd(context, component);

        final HtmlTable table = (HtmlTable) component;
        final ResponseWriter writer = context.getResponseWriter();
        final List<HtmlColumn> columns = table.getCachedColumns();

        if (!columns.isEmpty()) {
            writer.endElement("tbody");
            writer.endElement("table");
            writer.endElement("div");
        }
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
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

        if (StringUtils.isNotEmpty(behaviorEvent)) {
            if (params.get("params").startsWith("select_")) {
                final Integer rowIndex = convertStringToInteger(params.get("params").replaceFirst("select_", ""));
                final Object value = findRowValue((Iterable) untypedTableValue, rowIndex);

                if (value != null) {
                    final TableSingleSelectionListener listener = table.getSingleSelectionListener();

                    if (listener != null) {
                        listener.processTableSelection(value);
                    }
                }
            } else if (behaviorEvent.startsWith("sort")) {
                // TODO switch behaviorevent to params? (like select_)
                final Integer colIndex = convertStringToInteger(params.get("params"));
                final HtmlColumn sortedColumn = table.getCachedColumns().get(colIndex);
                final String tableUniqueIdentifier = table.getModelUniqueIdentifier();
                final String columnUniqueIdentifier = sortedColumn.getModelUniqueIdentifier();
                if (table.getTableSortModel().getSortType(tableUniqueIdentifier, columnUniqueIdentifier) == SortType.ASCENDING) {
                    table.getTableSortModel().sortColumn(tableUniqueIdentifier, columnUniqueIdentifier, sortedColumn.getSortBy(), SortType.DESCENDING);
                } else {
                    table.getTableSortModel().sortColumn(tableUniqueIdentifier, columnUniqueIdentifier, sortedColumn.getSortBy(), SortType.ASCENDING);
                }
            }
        }
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        final HtmlTable table = (HtmlTable) component;
        final ResponseWriter writer = context.getResponseWriter();

        final AjaxBehavior clickAjaxBehavior = ClientBehaviorResolver.resolveActiveAjaxBehavior(table, "click");

        // row ordering could be changed by creating table header (read sort model)
        table.resetDataModel();

        try {
            DataVisitor visitor = new DataVisitor() {
                public DataVisitResult process(FacesContext context, Integer rowKey) throws IOException {
                    table.setRowKey(context, rowKey);

                    if (table.isRowAvailable() && table.getChildCount() > 0) {
                        writer.startElement("tr", table);
                        writer.writeAttribute("rowindex", rowKey, null);
                        final String rowClass = StringJoiner.on(' ').join("butter-table-row").join(StringUtils.getNullSafeValue(table.getRowClass())).toString();
                        writer.writeAttribute("class", rowClass, null);

                        if (clickAjaxBehavior != null && table.getSingleSelectionListener() != null) {
                            final JsfAjaxRequest ajaxRequest = new JsfAjaxRequest(table.getClientId(), true)
                                    .setEvent("click")
                                    .setRender(table, "click")
                                    .setParams("'select_" + rowKey + "'")
                                    .setBehaviorEvent("click");
                            final String jQueryPluginCall = RenderUtils.createJQueryPluginCall(table.getClientId(), "selectTableRow({rowIndex:'" + rowKey + "'})");
                            writer.writeAttribute("onclick", ajaxRequest.toString() + ";" + jQueryPluginCall, null);
                        }

                        for (UIComponent child : table.getCachedColumns()) {
                            child.encodeAll(context);
                        }

                        writer.endElement("tr");
                    }

                    return DataVisitResult.CONTINUE;
                }
            };

            table.walk(context, visitor);
        } finally {
            table.setRowKey(context, null);
        }
    }

    private void renderHeaderColGroup(HtmlTable table, ResponseWriter writer) throws IOException {
        writer.startElement("colgroup", table);

        int columnNumber = 0;

        for (HtmlColumn column : table.getCachedColumns()) {
            writer.startElement("col", table);
            writer.writeAttribute("class", "butter-table-colgroup", null);
            writer.writeAttribute("columnNumber", "" + columnNumber, null);

            final StringBuilder style = new StringBuilder();

            if (StringUtils.isNotEmpty(column.getColWidth())) {
                style.append("width: ");
                style.append(column.getColWidth());
            }
            if (table.isHideColumn(column)) {
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

    private void encodeColumnHeader(HtmlTable table,
                                    int columnNumber,
                                    HtmlColumn column,
                                    FacesContext context) throws IOException {
        final ResponseWriter writer = context.getResponseWriter();
        final WebXmlParameters webXmlParameters = new WebXmlParameters(context.getExternalContext());

        writer.startElement("th", table);
        if (column.isSortColumnEnabled() && table.getTableSortModel() != null) {
            writer.writeAttribute("class", "butter-component-table-column-header butter-component-table-column-sort", null);
        } else {
            writer.writeAttribute("class", "butter-component-table-column-header", null);
        }
        writer.writeAttribute("columnNumber", "" + columnNumber, null);

        if (table.isHideColumn(column)) {
            writer.writeAttribute("style", "display:none", null);
        }

        // TODO check if ajax child is present
        if (column.isSortColumnEnabled() && table.getModel() != null) {
            final String ajax = TableToolbarRenderer.createModelJavaScriptCall(table.getClientId(), Arrays.asList(table.getClientId()), "sortTableRow", isAjaxDisableRenderReqionOnRequest(table, webXmlParameters), columnNumber + "");
            writer.writeAttribute("onclick", ajax, null);
        }

        final HtmlTooltip tooltip = this.findTooltip(column);

        writer.startElement("div", table);
        if (tooltip != null) {
            writer.writeAttribute("data-tooltip-identifier", this.createTooltipIdentifier(column), null);
        }
        writer.writeAttribute("class", column.getHeaderStyleClass(), null);
        writer.writeAttribute("style", column.getHeaderStyle(), null);

        // render header label
        writer.startElement("span", table);
        writer.writeAttribute("class", "butter-component-table-column-label", "styleclass");
        writer.writeText(column.getLabel(), null);
        writer.endElement("span");

        // TODO check if ajax child is present
        if (column.isSortColumnEnabled() && table.getTableSortModel() != null) {
            writer.startElement("span", table);
            final String tableUniqueIdentifier = table.getModelUniqueIdentifier();
            final String columnUniqueIdentifier = column.getModelUniqueIdentifier();
            final SortType sortType = table.getModel().getTableRowSortingModel().getSortType(tableUniqueIdentifier, columnUniqueIdentifier);

            StringJoiner styleClasses = StringJoiner.on(' ').join("butter-component-table-column-sort-spinner");

            if (sortType == SortType.ASCENDING) {
                styleClasses = styleClasses.join(webXmlParameters.getSortAscGlyphicon());
            } else if (sortType == SortType.DESCENDING) {
                styleClasses = styleClasses.join(webXmlParameters.getSortDescGlyphicon());
            } else {
                styleClasses = styleClasses.join(webXmlParameters.getSortUnknownGlyphicon());
            }

            writer.writeAttribute("class", styleClasses.toString(), null);
            writer.endElement("span");
        }

        if (tooltip != null) {
            tooltip.setFor("[data-tooltip-identifier=\"" + this.createTooltipIdentifier(column) + "\"]");
            tooltip.encodeAll(context);
        }

        writer.endElement("div");
        writer.endElement("th");
    }

    private Boolean isAjaxDisableRenderReqionOnRequest(final HtmlTable table, final WebXmlParameters parameters) {
        final Boolean disableRegion = table.isAjaxDisableRenderRegionsOnRequest();
        return disableRegion != null ? disableRegion : parameters.isAjaxDisableRenderRegionsOnRequest();
    }

    private String createTooltipIdentifier(HtmlColumn column) {
        return column.getClientId() + "_div";
    }

    private HtmlTooltip findTooltip(final HtmlColumn column) {
        for (UIComponent uiComponent : column.getChildren()) {
            if (uiComponent instanceof HtmlTooltip) {
                return (HtmlTooltip) uiComponent;
            }
        }

        return null;
    }

    private String createBootstrapTableStyleClasses(final HtmlTable table) {
        StringJoiner stringJoiner = StringJoiner.on(' ')
                .join("table")
                .join("table-hover");

        if (table.isTableCondensed()) {
            stringJoiner = stringJoiner.join("table-condensed");
        }
        if (table.isTableBordered()) {
            stringJoiner = stringJoiner.join("table-bordered");
        }
        if (table.isTableStriped()) {
            stringJoiner = stringJoiner.join("table-striped");
        }

        return stringJoiner.toString();
    }

    private boolean hasColumnWidthSet(final List<HtmlColumn> columns) {
        for (HtmlColumn column : columns) {
            if (StringUtils.isNotEmpty(column.getColWidth())) {
                return true;
            }
        }

        return false;
    }

    private Object findRowValue(final Iterable value, final int row) {
        final Iterator iterator = value.iterator();
        int actualRow = 0;

        while (iterator.hasNext()) {
            final Object actualRowValue = iterator.next();

            if (actualRow == row) {
                return actualRowValue;
            }

            actualRow++;
        }

        return null;
    }

    private Integer convertStringToInteger(final String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
