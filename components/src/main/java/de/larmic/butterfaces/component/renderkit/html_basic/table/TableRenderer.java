package de.larmic.butterfaces.component.renderkit.html_basic.table;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;
import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.component.html.table.TableSingleSelectionListener;
import de.larmic.butterfaces.component.partrenderer.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.*;

/**
 * Created by larmic on 10.09.14.
 */
@FacesRenderer(componentFamily = HtmlTable.COMPONENT_FAMILY, rendererType = HtmlTable.RENDERER_TYPE)
public class TableRenderer extends HtmlBasicRenderer {

    private static final Attribute[] ATTRIBUTES =
            AttributeManager.getAttributes(AttributeManager.Key.DATATABLE);

    @Override
    protected boolean shouldEncode(UIComponent component) {
        return super.shouldEncode(component) && !getColumns(component).isEmpty();
    }

    @Override
    public void encodeBegin(final FacesContext context, final UIComponent component) throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        HtmlTable table = (HtmlTable) component;
        table.setRowIndex(-1);

        // Render the beginning of the table
        ResponseWriter writer = context.getResponseWriter();

        renderTableStart(context, component, writer, ATTRIBUTES);
        renderHeader(table, writer);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
            throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncode(component)) {
            return;
        }

        ((UIData) component).setRowIndex(-1);

        // Render the ending of this table
        renderTableEnd(component, context.getResponseWriter());
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        rendererParamsNotNull(context, component);

        if (!shouldEncodeChildren(component)) {
            return;
        }

        final UIData data = (UIData) component;

        ResponseWriter writer = context.getResponseWriter();

        renderTableBodyStart(component, writer);

        // Iterate over the rows of data that are provided
        int processed = 0;
        int rowIndex = data.getFirst() - 1;
        int rows = data.getRows();
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
            renderRowStart(component, rowIndex, writer);

            // Render the row content
            renderRow(context, component, writer);

            // Render the ending of this row
            renderRowEnd(component, writer);
        }

        renderTableBodyEnd(component, writer);

        // Clean up after ourselves
        data.setRowIndex(-1);
    }

    @Override
    public void decode(final FacesContext context, final UIComponent component) {
        final HtmlTable htmlTable = (HtmlTable) component;
        final Map<String, List<ClientBehavior>> behaviors = htmlTable.getClientBehaviors();

        if (behaviors.isEmpty()) {
            return;
        }

        final Object untypedTableValue = ((HtmlTable) component).getValue();
        final TableSingleSelectionListener listener = ((HtmlTable) component).getSingleSelectionListener();

        if (listener == null) {
            return;
        }

        if (!(untypedTableValue instanceof Iterable)) {
            return;
        }

        final Iterable tableValues = (Iterable) untypedTableValue;

        final ExternalContext external = context.getExternalContext();
        final Map<String, String> params = external.getRequestParameterMap();
        final String behaviorEvent = params.get("javax.faces.behavior.event");

        if (behaviorEvent != null) {
            final String[] split = behaviorEvent.split("_");
            final String event = split[0];
            final String nodeNumber = split[1];

            final Object rowObject = findRowObject(tableValues, Integer.valueOf(nodeNumber));

            if (rowObject != null) {
                listener.processValueChange(rowObject);
            }
        }
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

    protected void renderHeader(final HtmlTable table, final ResponseWriter writer) throws IOException {
        final List<HtmlColumn> columns = getColumns(table);

        writer.startElement("thead", table);
        writer.startElement("tr", table);
        for (HtmlColumn column : columns) {
            writer.startElement("th", table);
            writer.writeAttribute("id", column.getClientId(), null);
            writer.writeText(column.getLabel(), null);
            writer.endElement("th");
        }
        writer.endElement("tr");
        writer.endElement("thead");
    }

    protected void renderTableStart(final FacesContext context, final UIComponent table, final ResponseWriter writer, final Attribute[] attributes) throws IOException {
        writer.startElement("table", table);
        writeIdAttributeIfNecessary(context, writer, table);
        final String styleClass = (String) table.getAttributes().get("styleClass");
        if (styleClass != null) {
            writer.writeAttribute("class", "table table-striped table-hover " + styleClass, "styleClass");
        } else {
            writer.writeAttribute("class", "table table-striped table-hover", "styleClass");
        }

        // RenderKitUtils.renderPassThruAttributes(context, writer, table, attributes);
        writer.writeText("\n", table, null);

    }

    protected void renderTableEnd(final UIComponent table, final ResponseWriter writer) throws IOException {
        writer.endElement("table");
        writer.writeText("\n", table, null);
    }

    protected void renderRowStart(final UIComponent component, final int rowIndex, final ResponseWriter writer) throws IOException {
        final HtmlTable htmlTable = (HtmlTable) component;
        final FacesContext context = FacesContext.getCurrentInstance();
        final ClientBehaviorContext behaviorContext =
                ClientBehaviorContext.createClientBehaviorContext(context,
                        htmlTable, "click", component.getClientId(context), null);

        final String clientId = htmlTable.getClientId();
        final String baseClientId = clientId.substring(0, clientId.length() - (rowIndex+"").length() - 1);

        writer.startElement("tr", htmlTable);
        writer.writeAttribute("rowIndex", rowIndex, null);

        final Map<String, List<ClientBehavior>> behaviors = htmlTable.getClientBehaviors();
        if (behaviors.containsKey("click")) {
            final String click = behaviors.get("click").get(0).getScript(behaviorContext);

            if (StringUtils.isNotEmpty(click)) {
                final String correctedEventName = click.replace(",'click',", ",'click_" + rowIndex + "',");
                final String correctedClientId = correctedEventName.replaceFirst(clientId, baseClientId);
                writer.writeAttribute("onclick", correctedClientId, null);
            }
        }

        writer.writeText("\n", htmlTable, null);
    }

    protected void renderRowEnd(final UIComponent table, final ResponseWriter writer) throws IOException {
        writer.endElement("tr");
        writer.writeText("\n", table, null);
    }

    protected void renderTableBodyStart(final UIComponent table, final ResponseWriter writer) throws IOException {
        writer.startElement("tbody", table);
        writer.writeText("\n", table, null);
    }

    protected void renderTableBodyEnd(final UIComponent table, final ResponseWriter writer) throws IOException {
        writer.endElement("tbody");
        writer.writeText("\n", table, null);
    }

    protected void renderRow(final FacesContext context,
                             final UIComponent table,
                             final ResponseWriter writer) throws IOException {
        // Iterate over the child HtmlColumn components for each row
        for (HtmlColumn column : getColumns(table)) {
            writer.startElement("td", table);
            // Render the contents of this cell by iterating over
            // the kids of our kids
            for (Iterator<UIComponent> gkids = getChildren(column); gkids.hasNext(); ) {
                encodeRecursive(context, gkids.next());
            }

            writer.writeText("\n", table, null);
            writer.endElement("td");
        }
    }

    /**
     * <p>Return an Iterator over the <code>HtmlColumn</code> children of the
     * specified <code>UIData</code> that have a <code>rendered</code> property
     * of <code>true</code>.</p>
     *
     * @param table the table from which to extract children
     * @return the List of all HtmlColumn children
     */
    private List<HtmlColumn> getColumns(final UIComponent table) {
        final int childCount = table.getChildCount();
        if (childCount > 0) {
            final List<HtmlColumn> results = new ArrayList<>(childCount);
            for (UIComponent kid : table.getChildren()) {
                if ((kid instanceof HtmlColumn) && kid.isRendered()) {
                    results.add((HtmlColumn) kid);
                }
            }
            return results;
        } else {
            return Collections.emptyList();
        }
    }
}
