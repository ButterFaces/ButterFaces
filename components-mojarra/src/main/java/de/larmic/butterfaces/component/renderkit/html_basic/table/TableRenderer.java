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

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    private boolean systemIdentifierHashcodeIsUsedAsCachedRowIdentifier;
    private String cachedRowIdentifier;

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
            writer.startElement("th", table);
            writer.writeAttribute("id", column.getClientId(), null);
            if (column.isSortColumnEnabled() && htmlTable.getTableSortModel() != null) {
                writer.writeAttribute("class", "butter-component-table-column-header butter-component-table-column-sort", null);
            } else {
                writer.writeAttribute("class", "butter-component-table-column-header", null);
            }
            writer.writeAttribute("columnNumber", "" + columnNumber, null);

            if (this.isHideColumn(htmlTable, column)) {
                writer.writeAttribute("style", "display:none", null);
            }

            if (column.isSortColumnEnabled() && htmlTable.getModel() != null && ajaxRequest != null) {
                writer.writeAttribute("onclick", ajaxRequest.createJavaScriptCall("sort_" + columnNumber, htmlTable.isAjaxDisableRenderRegionsOnRequest()), null);
            }

            writer.startElement("div", table);

            // render header label
            writer.startElement("span", table);
            writer.writeAttribute("class", "butter-component-table-column-label", null);
            writer.writeText(column.getLabel(), null);
            writer.endElement("span");

            if (column.isSortColumnEnabled() && htmlTable.getTableSortModel() != null && ajaxRequest != null) {
                writer.startElement("span", table);
                final String tableUniqueIdentifier = StringUtils.getNotNullValue(htmlTable.getUniqueIdentifier(), table.getId());
                final String columnUniqueIdentifier = StringUtils.getNotNullValue(column.getUniqueIdentifier(), column.getId());
                final SortType sortType = htmlTable.getModel().getTableSortModel().getSortType(tableUniqueIdentifier, columnUniqueIdentifier);

                final StringBuilder sortSpanStyleClass = new StringBuilder("butter-component-table-column-sort-spinner ");

                if (sortType == SortType.ASCENDING) {
                    sortSpanStyleClass.append(" " + webXmlParameters.getSortAscGlyphicon());
                } else if (sortType == SortType.DESCENDING) {
                    sortSpanStyleClass.append(" " + webXmlParameters.getSortDescGlyphicon());
                } else {
                    sortSpanStyleClass.append(" " + webXmlParameters.getSortUnknownGlyphicon());
                }

                writer.writeAttribute("class", sortSpanStyleClass.toString(), null);
                writer.endElement("span");
            }

            writer.endElement("div");

            writer.endElement("th");

            columnNumber++;
        }
        writer.endElement("tr");
        writer.endElement("thead");
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
        if (isRowSelected(context, htmlTable.getVar(), htmlTable.getRowIdentifierProperty())) {
            writer.writeAttribute("class", rowClass + " butter-table-row-selected", null);
        } else {
            writer.writeAttribute("class", rowClass, null);
        }

        final Map<String, List<ClientBehavior>> behaviors = htmlTable.getClientBehaviors();
        if (behaviors.containsKey("click")) {
            final String click = behaviors.get("click").get(0).getScript(behaviorContext);

            if (StringUtils.isNotEmpty(click) && htmlTable.getSingleSelectionListener() != null) {
                final String correctedEventName = click.replace(",'click',", ",'click_" + rowIndex + "',");
                final String correctedClientId = correctedEventName.replaceFirst(clientId, baseClientId);
                final String jQueryPluginCall = RenderUtils.createJQueryPluginCall(htmlTable.getClientId(), "selectRow({rowIndex:'" + rowIndex + "'})");
                writer.writeAttribute("onclick", correctedClientId + ";" + jQueryPluginCall.replaceFirst(clientId, baseClientId), null);
            }
        }

        writer.writeText("\n", htmlTable, null);

        rowIndex++;
    }

    private boolean isRowSelected(final FacesContext context, final String htmlTableVar, final String rowIdentifierProperty) {
        if (StringUtils.isNotEmpty(cachedRowIdentifier)) {
            final String value = this.createRowIdentifierValueExpression(context, htmlTableVar, rowIdentifierProperty);
            return cachedRowIdentifier.equals(value);
        }

        return false;
    }

    private String createRowIdentifierValueExpression(final FacesContext context, String htmlTableVar, String rowIdentifierProperty) {
        final ELContext elContext = context.getELContext();
        final String valueExpressionString = systemIdentifierHashcodeIsUsedAsCachedRowIdentifier ? "#{" + htmlTableVar + "}" : "#{" + htmlTableVar + "." + rowIdentifierProperty + "}";
        final ValueExpression valueExpression = this.createRowIdentifierValueExpression(context, valueExpressionString);
        final Object value = valueExpression.getValue(elContext);
        return systemIdentifierHashcodeIsUsedAsCachedRowIdentifier ? System.identityHashCode(value) + "" : value.toString();
    }

    private ValueExpression createRowIdentifierValueExpression(final FacesContext context, final String valueExpression) {
        final ELContext elContext = context.getELContext();
        return context.getApplication().getExpressionFactory().createValueExpression(elContext, valueExpression, Object.class);
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final HtmlTable htmlTable = (HtmlTable) component;
        final Map<String, List<ClientBehavior>> behaviors = htmlTable.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return;
        }

        final Object untypedTableValue = htmlTable.getValue();


        if (!(untypedTableValue instanceof Iterable)) {
            return;
        }

        final Iterable tableValues = (Iterable) untypedTableValue;

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (behaviorEvent != null && behaviorEvent.contains("_")) {
            final String[] split = behaviorEvent.split("_");
            final String event = split[0];
            try {
                final int eventNumber = Integer.valueOf(split[1]);
                if ("click".equals(event)) {
                    final Object rowObject = findRowObject(tableValues, eventNumber);

                    cachedRowIdentifier = null;

                    if (rowObject != null) {
                        final TableSingleSelectionListener listener = htmlTable.getSingleSelectionListener();

                        if (listener != null) {
                            listener.processTableSelection(rowObject);
                            final String rowIdentifier = this.getRowIdentifierProperty(rowObject, htmlTable.getRowIdentifierProperty());
                            cachedRowIdentifier = rowIdentifier;
                        }

                    }
                } else if ("sort".equals(event) && htmlTable.getModel() != null) {
                    final HtmlColumn sortedColumn = htmlTable.getCachedColumns().get(eventNumber);
                    final String tableUniqueIdentifier = StringUtils.getNotNullValue(htmlTable.getUniqueIdentifier(), htmlTable.getId());
                    final String columnUniqueIdentifier = StringUtils.getNotNullValue(sortedColumn.getUniqueIdentifier(), sortedColumn.getId());
                    if (htmlTable.getTableSortModel().getSortType(tableUniqueIdentifier, columnUniqueIdentifier) == SortType.ASCENDING) {
                        htmlTable.getTableSortModel().sortColumn(tableUniqueIdentifier, columnUniqueIdentifier, sortedColumn.getSortBy(), SortType.DESCENDING);
                    } else {
                        htmlTable.getTableSortModel().sortColumn(tableUniqueIdentifier, columnUniqueIdentifier, sortedColumn.getSortBy(), SortType.ASCENDING);
                    }
                }
            } catch (NumberFormatException e) {
                // event is not fired by table component
            }
        }
    }

    private String getRowIdentifierProperty(final Object rowObject, final String rowIdentifierProperty) {
        systemIdentifierHashcodeIsUsedAsCachedRowIdentifier = false;

        if (StringUtils.isNotEmpty(rowIdentifierProperty)) {
            String identifier = getRowIdentifierPropertyByField(rowObject, rowIdentifierProperty);

            if (StringUtils.isEmpty(identifier)) {
                identifier = getRowIdentifierPropertyByGetter(rowObject, rowIdentifierProperty);
            }

            if (StringUtils.isNotEmpty(identifier)) {
                return identifier;
            }
        }

        systemIdentifierHashcodeIsUsedAsCachedRowIdentifier = true;
        return System.identityHashCode(rowObject) + "";
    }

    private String getRowIdentifierPropertyByField(Object rowObject, String rowIdentifierProperty) {
        try {
            final Method method = rowObject.getClass().getMethod("get" + toUpperCase(rowIdentifierProperty));
            final Object valueObject = method.invoke(rowObject, (Object[]) null);
            return convertRowIdentifierToString(valueObject);
        } catch (NoSuchMethodException e) {
        } catch (InvocationTargetException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    private String toUpperCase(final String str) {
        return Character.toString(str.charAt(0)).toUpperCase()+str.substring(1);
    }

    private String getRowIdentifierPropertyByGetter(Object rowObject, String rowIdentifierProperty) {
        try {
            final Field declaredField = rowObject.getClass().getDeclaredField(rowIdentifierProperty);
            declaredField.setAccessible(true);
            return convertRowIdentifierToString(declaredField.get(rowObject));
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    private String convertRowIdentifierToString(final Object rowIdentifier) throws IllegalAccessException {
        if (rowIdentifier != null) {
            final String rowIdentifierAsString = rowIdentifier.toString();

            if (StringUtils.isNotEmpty(rowIdentifierAsString)) {
                return rowIdentifierAsString;
            }
        }

        return null;
    }

    private Object findRowObject(final Iterable tableValues, final int row) {
        final Iterator iterator = tableValues.iterator();
        int actualRow = 0;

        while (iterator.hasNext()) {
            final Object value = iterator.next();

            if (actualRow == row) {
                return value;
            }

            actualRow++;
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
