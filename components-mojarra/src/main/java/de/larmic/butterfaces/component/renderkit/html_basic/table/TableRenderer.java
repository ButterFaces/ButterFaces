package de.larmic.butterfaces.component.renderkit.html_basic.table;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.RenderKitUtils;
import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.partrenderer.StringUtils;
import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.model.table.SortType;
import de.larmic.butterfaces.resolver.AjaxRequest;
import de.larmic.butterfaces.resolver.AjaxRequestFactory;
import de.larmic.butterfaces.resolver.WebXmlParameters;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
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
public class TableRenderer extends de.larmic.butterfaces.component.renderkit.html_basic.mojarra.TableRenderer {

    private boolean hasColumnWidthSet;
    // dirty: method renderRowStart does not have a rowIndex parameter but it should.
    // alternative: copy encode children but this means coping a lot of private methods... :(
    // we will try this way... maybe migrating later...
    private int rowIndex;

    private WebXmlParameters webXmlParameters;

    @Override
    public void encodeBegin(final FacesContext context,
                            final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        final HtmlTable table = (HtmlTable) component;
        this.hasColumnWidthSet = hasColumnWidthSet(table.getCachedColumns());
        this.rowIndex = 0;
        this.webXmlParameters = new WebXmlParameters(context.getExternalContext());

        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);

        final ResponseWriter responseWriter = context.getResponseWriter();

        RenderUtils.renderJQueryPluginCall(component.getClientId(), "fixBootstrapDropDown()", responseWriter, component);
    }

    @Override
    protected void renderHeader(final FacesContext context,
                                final UIComponent table,
                                final ResponseWriter writer) throws IOException {
        final HtmlTable htmlTable = (HtmlTable) table;

        if (hasColumnWidthSet) {

            writer.startElement("colgroup", table);

            int columnNumber = 0;

            for (HtmlColumn column : htmlTable.getCachedColumns()) {
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

        final AjaxRequest ajaxRequest = new AjaxRequestFactory().createRequest(htmlTable, "click");
        if (ajaxRequest != null) {
            ajaxRequest.getRenderIds().add(htmlTable.getClientId());
        }

        for (HtmlColumn column : htmlTable.getCachedColumns()) {
            column.setWebXmlParameters(webXmlParameters);
            column.setColumnNumberUsedByTable(columnNumber);
            column.setTableAjaxClickRequest(ajaxRequest);
            column.encodeAll(context);
            columnNumber++;
        }
        writer.endElement("tr");
        writer.endElement("thead");
    }

    private boolean isHideColumn(final HtmlTable table, final HtmlColumn column) {
        if (table.getTableColumnDisplayModel() != null) {
            final String tableUniqueIdentifier = table.getModelUniqueIdentifier();
            final String columnUniqueIdentifier = column.getModelUniqueIdentifier();
            final Boolean hideColumn = table.getTableColumnDisplayModel().isColumnHidden(tableUniqueIdentifier, columnUniqueIdentifier);
            if (hideColumn != null) {
                return hideColumn;
            }
        }
        return column.isHideColumn();
    }

    @Override
    protected void renderTableStart(final FacesContext context,
                                    final UIComponent component,
                                    final ResponseWriter writer,
                                    final Attribute[] attributes) throws IOException {
        final HtmlTable table = (HtmlTable) component;

        writer.startElement("div", table);
        writer.writeAttribute("id", component.getClientId(context), "id");
        writer.writeAttribute("class", "butter-table", null);

        writer.startElement("div", table);

        final String styleClass = (String) table.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", "table-responsive " + styleClass, "styleClass");
        } else {
            writer.writeAttribute("class", "table-responsive", "styleClass");
        }

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

        RenderKitUtils.renderPassThruAttributes(context, writer, table, attributes);
        writer.writeText("\n", table, null);
    }

    @Override
    protected void renderTableEnd(final FacesContext context,
                                  final UIComponent component,
                                  final ResponseWriter writer) throws IOException {
        super.renderTableEnd(context, component, writer);
        writer.endElement("div");
        writer.endElement("div");
    }

    @Override
    protected void renderRowStart(final FacesContext context,
                                  final UIComponent component,
                                  final ResponseWriter writer) throws IOException {
        final HtmlTable htmlTable = (HtmlTable) component;
        final ClientBehaviorContext behaviorContext =
                ClientBehaviorContext.createClientBehaviorContext(context,
                        htmlTable, "click", component.getClientId(context), null);

        final String clientId = htmlTable.getClientId();
        final String baseClientId = clientId.substring(0, clientId.length() - (rowIndex + "").length() - 1);

        writer.startElement("tr", htmlTable);
        writer.writeAttribute("rowIndex", rowIndex, null);
        final String rowClass = StringUtils.isNotEmpty(htmlTable.getRowClass()) ? "butter-table-row " + htmlTable.getRowClass() : "butter-table-row";

        // TODO maybe optimize -> isRowSelected is expensive. If first selected row is found no other calls are needed
        // TODO because table only support row single selection

        if (this.isRowSelected(htmlTable, rowIndex)) {
            writer.writeAttribute("class", rowClass + " butter-table-row-selected", null);
        } else {
            writer.writeAttribute("class", rowClass, null);
        }

        final Map<String, List<ClientBehavior>> behaviors = htmlTable.getClientBehaviors();
        if (behaviors.containsKey("click")) {
            final String click = behaviors.get("click").get(0).getScript(behaviorContext);

            if (StringUtils.isNotEmpty(click) && htmlTable.getSingleSelectionListener() != null) {
                final String ajaxCall = new AjaxRequest(htmlTable, "click").createJavaScriptCall("click_" + rowIndex);
                final String aThis = ajaxCall.replaceFirst(clientId, baseClientId);
                final String jQueryPluginCall = RenderUtils.createJQueryPluginCall(htmlTable.getClientId(), "selectRow({rowIndex:'" + rowIndex + "'})");
                writer.writeAttribute("onclick", aThis + ";" + jQueryPluginCall.replaceFirst(clientId, baseClientId), null);
            }
        }

        writer.writeText("\n", htmlTable, null);

        rowIndex++;
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
                    final HtmlColumn sortedColumn = table.getCachedColumns().get(eventNumber);
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
