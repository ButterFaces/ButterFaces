package de.larmic.butterfaces.component.showcase.table;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.CssCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.tree.RowIdentifierType;
import de.larmic.butterfaces.component.showcase.tree.SelectionAjaxType;
import de.larmic.butterfaces.event.TableSingleSelectionListener;
import de.larmic.butterfaces.model.table.DefaultTableModel;
import de.larmic.butterfaces.model.table.SortType;
import org.apache.commons.lang3.StringUtils;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
public class TableShowcase extends AbstractCodeShowcase implements Serializable, TableSingleSelectionListener<StringPair> {

    private final List<StringPair> stringPairs = new ArrayList<>();
    private StringPair selectedValue = null;
    private String doSomethingWithRow = null;
    private SelectionAjaxType selectionAjaxType = SelectionAjaxType.AJAX;
    private FourthColumnWidthType fourthColumnWidthType = FourthColumnWidthType.NONE;
    private RowIdentifierType rowIdentifierType = RowIdentifierType.ID;
    private TableModelType tableModelType = TableModelType.DEFAULT_MODEL;
    private ToolBarType toolBarType = ToolBarType.SERVER_FILTER;
    private DefaultTableModel tableModel = new DefaultTableModel();

    private String rowIdentifierProperty;
    private boolean tableCondensed;
    private boolean tableBordered;
    private boolean tableStriped = true;
    private boolean showRefreshButton = true;
    private boolean showToggleColumnButton = true;
    private boolean ajaxDisableRenderRegionsOnRequest = true;
    private String filterValue;
    private String colWidthColumn1;
    private String colWidthColumn2;
    private String colWidthColumn3;
    private String colWidthColumn4;

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        codeExamples.add(this.createXhtmlCodeExample());
        codeExamples.add(this.createMyBeanCodeExample());
        codeExamples.add(this.createStringPairCodeExample());

        if (this.toolBarType == ToolBarType.TEXT) {
            final CssCodeExample cssCodeExample = new CssCodeExample();
            cssCodeExample.addCss(".butter-table-toolbar-custom", "margin-top: 10px;");
            codeExamples.add(cssCodeExample);
        }

    }

    public List<StringPair> getStringRows() {
        if (stringPairs.isEmpty()) {
            stringPairs.add(new StringPair(1L, "r1c1", "r1c2"));
            stringPairs.add(new StringPair(2L, "r2c1", "r2c2"));
            stringPairs.add(new StringPair(3L, "r3c1", "r3c2"));
            stringPairs.add(new StringPair(4L, "r4c1", "r4c2"));
            stringPairs.add(new StringPair(5L, "r5c1", "r5c2"));
            stringPairs.add(new StringPair(6L, "r6c1", "r6c2"));
            stringPairs.add(new StringPair(7L, "r7c1", "r7c2"));
        }

        if (toolBarType == ToolBarType.SERVER_FILTER && StringUtils.isNotEmpty(filterValue)) {
            final List<StringPair> filteredStringPairs = new ArrayList<>();

            for (StringPair stringPair : stringPairs) {
                if (StringUtils.containsIgnoreCase(stringPair.getA(), filterValue)
                        || StringUtils.containsIgnoreCase(stringPair.getB(), filterValue)) {
                    filteredStringPairs.add(stringPair);
                }
            }

            return filteredStringPairs;
        }

        if (this.shouldReverseRows()) {
            Collections.reverse(stringPairs);
        }

        return stringPairs;
    }

    private boolean shouldReverseRows() {
        if ((this.tableModel.getTableSortModel().getSortType("column1") == SortType.ASCENDING
                || this.tableModel.getTableSortModel().getSortType("column2") == SortType.ASCENDING)
                && !stringPairs.get(0).getA().equals("r1c1")) {
            return true;
        } else if ((this.tableModel.getTableSortModel().getSortType("column1") == SortType.DESCENDING
                || this.tableModel.getTableSortModel().getSortType("column2") == SortType.DESCENDING)
                && stringPairs.get(0).getA().equals("r1c1")) {
            return true;
        }


        return false;
    }

    @Override
    public void processValueChange(final StringPair data) {
        this.selectedValue = data;
    }

    private XhtmlCodeExample createXhtmlCodeExample() {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(true);
        xhtmlCodeExample.appendInnerContent("        <b:tableToolbar tableId=\"input\"");
        xhtmlCodeExample.appendInnerContent("                        showRefreshButton=\"" + this.showRefreshButton + "\"");
        xhtmlCodeExample.appendInnerContent("                        showToggleColumnButton=\"" + this.showToggleColumnButton + "\"");
        xhtmlCodeExample.appendInnerContent("                        ajaxDisableRenderRegionsOnRequest=\"" + this.ajaxDisableRenderRegionsOnRequest + "\"");
        xhtmlCodeExample.appendInnerContent("                        rendered=\"" + this.isRendered() + ">");
        xhtmlCodeExample.appendInnerContent("            <!-- at this time you have to put an ajax tag to activate some features-->");
        xhtmlCodeExample.appendInnerContent("            <f:ajax />");
        if (this.toolBarType == ToolBarType.TEXT) {
            xhtmlCodeExample.appendInnerContent("            Custom toolbar text...");
        } else if (this.toolBarType == ToolBarType.SERVER_FILTER) {
            xhtmlCodeExample.appendInnerContent("            <b:text value=\"#{myBean.filterValue}\"");
            xhtmlCodeExample.appendInnerContent("                    placeholder=\"Enter text...\"");
            xhtmlCodeExample.appendInnerContent("                    autoFocus=\"true\"");
            xhtmlCodeExample.appendInnerContent("                    hideLabel=\"true\">");
            xhtmlCodeExample.appendInnerContent("                <f:ajax event=\"keyup\" render=\"input\"/>");
            xhtmlCodeExample.appendInnerContent("            </b:text>");
        } else if (toolBarType == ToolBarType.CLIENT_FILTER) {
            xhtmlCodeExample.appendInnerContent("            <div class=\"form-inline pull-left\" role=\"form\">");
            xhtmlCodeExample.appendInnerContent("                <div class=\"form-group\">");
            xhtmlCodeExample.appendInnerContent("                    <input type=\"text\"");
            xhtmlCodeExample.appendInnerContent("                           class=\"form-control jQueryPluginSelector\"");
            xhtmlCodeExample.appendInnerContent("                           placeholder=\"Enter text...\"");
            xhtmlCodeExample.appendInnerContent("                           data-filterable-item-container=\".butter-table\"/>");
            xhtmlCodeExample.appendInnerContent("                </div>");
            xhtmlCodeExample.appendInnerContent("            </div>");
        }
        xhtmlCodeExample.appendInnerContent("        </b:tableToolbar>\n");

        xhtmlCodeExample.appendInnerContent("        <b:table id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                 var=\"rowItem\"");
        xhtmlCodeExample.appendInnerContent("                 value=\"#{myBean.value}\"");
        if (tableModelType == TableModelType.DEFAULT_MODEL) {
            xhtmlCodeExample.appendInnerContent("                 model=\"#{myBean.tableModel}\"");
        }
        if (selectionAjaxType == SelectionAjaxType.AJAX) {
            xhtmlCodeExample.appendInnerContent("                 singleSelectionListener=\"#{myBean}\"");
        }
        if (toolBarType == ToolBarType.CLIENT_FILTER) {
            xhtmlCodeExample.appendInnerContent("                 rowClass=\"filterable-item\"");
        }
        xhtmlCodeExample.appendInnerContent("                 tableBordered=\"" + this.tableBordered + "\"");
        xhtmlCodeExample.appendInnerContent("                 tableCondensed=\"" + this.tableCondensed + "\"");
        xhtmlCodeExample.appendInnerContent("                 tableStriped=\"" + this.tableStriped + "\"");
        if (rowIdentifierType == RowIdentifierType.ID) {
            xhtmlCodeExample.appendInnerContent("                 rowIdentifierProperty=\"id\"");
        }
        xhtmlCodeExample.appendInnerContent("                 rendered=\"" + this.isRendered() + "\">");

        xhtmlCodeExample.appendInnerContent("            <!-- at this time you have to put an ajax tag to activate some features-->");
        if (selectionAjaxType == SelectionAjaxType.AJAX) {
            xhtmlCodeExample.appendInnerContent("            <f:ajax render=\"formId:selectedRow\"/>");
        } else if (selectionAjaxType == SelectionAjaxType.AJAX_DISABLED) {
            xhtmlCodeExample.appendInnerContent("            <f:ajax render=\"formId:selectedRow\" disabled=\"true\"/>");
        }

        xhtmlCodeExample.appendInnerContent("            <column id=\"column1\"");
        if (fourthColumnWidthType == FourthColumnWidthType.PERCENT) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"10%\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.PX) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"50px\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.RELATIVE) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"5*\"");
        }
        xhtmlCodeExample.appendInnerContent("                    label=\"C1\">");
        xhtmlCodeExample.appendInnerContent("                /* text */");
        xhtmlCodeExample.appendInnerContent("            </column>");

        xhtmlCodeExample.appendInnerContent("            <column id=\"column2\"");
        if (fourthColumnWidthType == FourthColumnWidthType.PERCENT) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"75%\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.PX) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"30px\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.RELATIVE) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"1*\"");
        }
        xhtmlCodeExample.appendInnerContent("                    label=\"C2\">");
        xhtmlCodeExample.appendInnerContent("                /* input text */");
        xhtmlCodeExample.appendInnerContent("            </column>");

        xhtmlCodeExample.appendInnerContent("            <column id=\"column3\"");
        if (fourthColumnWidthType == FourthColumnWidthType.PERCENT) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"10%\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.PX) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"20px\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.RELATIVE) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"7*\"");
        }
        xhtmlCodeExample.appendInnerContent("                    sortColumnEnabled=\"false\"");
        xhtmlCodeExample.appendInnerContent("                    label=\"C3\">");
        xhtmlCodeExample.appendInnerContent("                /* action */");
        xhtmlCodeExample.appendInnerContent("            </column>");

        xhtmlCodeExample.appendInnerContent("            <column id=\"column4\"");
        if (fourthColumnWidthType == FourthColumnWidthType.PERCENT) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"5%\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.PX) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"10px\"");
        } else if (fourthColumnWidthType == FourthColumnWidthType.RELATIVE) {
            xhtmlCodeExample.appendInnerContent("                    colWidth=\"1*\"");
        }
        xhtmlCodeExample.appendInnerContent("                    sortColumnEnabled=\"false\"");
        xhtmlCodeExample.appendInnerContent("                    label=\"C4\">");
        xhtmlCodeExample.appendInnerContent("                /* text */");
        xhtmlCodeExample.appendInnerContent("            </column>");

        xhtmlCodeExample.appendInnerContent("        </b:table>");

        if (selectionAjaxType != SelectionAjaxType.NONE) {
            xhtmlCodeExample.appendInnerContent("\n        <h:panelGroup id=\"selectedRow\">");
            xhtmlCodeExample.appendInnerContent("            <h:output value=\"#{myBean.selectedRow.a}\"");
            xhtmlCodeExample.appendInnerContent("                      rendered=\"#{not empty myBean.selectedRow}\"/>");
            xhtmlCodeExample.appendInnerContent("        <h:panelGroup/>");
        }

        if (toolBarType == ToolBarType.CLIENT_FILTER) {
            xhtmlCodeExample.appendInnerContent("\n        /* activate client side filter jquery plugin */");
            xhtmlCodeExample.appendInnerContent("        <b:activateLibraries>");
            xhtmlCodeExample.appendInnerContent("        <script type=\"text/javascript\">");
            xhtmlCodeExample.appendInnerContent("            jQuery('.jQueryPluginSelector').butterItemFilterField();");
            xhtmlCodeExample.appendInnerContent("        </script>");
        }

        return xhtmlCodeExample;
    }

    private JavaCodeExample createMyBeanCodeExample() {
        final JavaCodeExample myBean = new JavaCodeExample("MyBean.java", "mybean", "table.demo", "MyBean", true);

        if (this.selectionAjaxType == SelectionAjaxType.AJAX) {
            myBean.addImport("de.larmic.butterfaces.event.TableSingleSelectionListener");
        }
        if (this.tableModelType == TableModelType.DEFAULT_MODEL) {
            myBean.addImport("de.larmic.butterfaces.model.table.TableModel");
            myBean.addImport("de.larmic.butterfaces.model.table.DefaultTableModel");
        }

        myBean.addImport("javax.faces.view.ViewScoped");
        myBean.addImport("javax.inject.Named");

        if (this.selectionAjaxType == SelectionAjaxType.AJAX) {
            myBean.addInterfaces("TableSingleSelectionListener");
        }

        if (this.tableModelType == TableModelType.DEFAULT_MODEL) {
            myBean.appendInnerContent("    private TableModel tableModel = new DefaultTableModel();\n");
        }
        if (this.toolBarType == ToolBarType.SERVER_FILTER) {
            myBean.appendInnerContent("    private String filterValue;\n");
        }

        myBean.appendInnerContent("    public List<StringPair> getValue() {");
        myBean.appendInnerContent("        final List<StringPair> pairs = new ArrayList<StringPair>();");
        if (rowIdentifierType == RowIdentifierType.ID) {
            myBean.appendInnerContent("        pairs.add(new StringPair(1L, \"r1c1\", \"r1c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(2L, \"r2c1\", \"r2c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(3L, \"r3c1\", \"r3c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(4L, \"r4c1\", \"r4c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(5L, \"r5c1\", \"r5c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(6L, \"r6c1\", \"r6c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(7L, \"r7c1\", \"r7c2\"));");
        } else {
            myBean.appendInnerContent("        pairs.add(new StringPair(\"r1c1\", \"r1c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(\"r2c1\", \"r2c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(\"r3c1\", \"r3c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(\"r4c1\", \"r4c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(\"r5c1\", \"r5c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(\"r6c1\", \"r6c2\"));");
            myBean.appendInnerContent("        pairs.add(new StringPair(\"r7c1\", \"r7c2\"));");
        }
        if (this.selectionAjaxType == SelectionAjaxType.AJAX && this.tableModelType == TableModelType.DEFAULT_MODEL) {
            myBean.appendInnerContent("        // TODO sort by table model");
        }
        if (this.toolBarType == ToolBarType.SERVER_FILTER) {
            myBean.appendInnerContent("        return this.filterByValue(pairs, this.filterValue);");
        } else {
            myBean.appendInnerContent("        return pairs;");
        }
        myBean.appendInnerContent("    }\n");

        if (selectionAjaxType == SelectionAjaxType.AJAX) {
            myBean.appendInnerContent("    private StringPair selectedRow;\n");
            myBean.appendInnerContent("    @Override");
            myBean.appendInnerContent("    public void processValueChange(final StringPair data) {");
            myBean.appendInnerContent("        this.selectedRow = data;");
            myBean.appendInnerContent("    }\n");
        }

        if (this.toolBarType == ToolBarType.SERVER_FILTER) {
            myBean.appendInnerContent("    public List<StringPair> filterByValue(final List<StringPair> pairs,");
            myBean.appendInnerContent("                                          final String filterValue) {");
            myBean.appendInnerContent("        // TODO implement me");
            myBean.appendInnerContent("        return pairs;");
            myBean.appendInnerContent("    }\n");
            myBean.appendInnerContent("    public String getFilterValue() {");
            myBean.appendInnerContent("        return this.filterValue;");
            myBean.appendInnerContent("    }\n");
            myBean.appendInnerContent("    public void setFilterValue(final String filterValue) {");
            myBean.appendInnerContent("        this.filterValue = filterValue;");
            myBean.appendInnerContent("    }\n");
        }

        if (selectionAjaxType == SelectionAjaxType.AJAX) {
            myBean.appendInnerContent("    public StringPair getSelectedRow() {");
            myBean.appendInnerContent("        return selectedRow;");
            myBean.appendInnerContent("    }\n");
        }

        if (this.tableModelType == TableModelType.DEFAULT_MODEL) {
            myBean.appendInnerContent("    public TableModel getTableModel() {");
            myBean.appendInnerContent("        return this.tableModel;");
            myBean.appendInnerContent("    }");
        }

        return myBean;
    }

    private JavaCodeExample createStringPairCodeExample() {
        final JavaCodeExample stringPair = new JavaCodeExample("StringPair.java", "stringpair", "table.demo", "StringPair", false);
        if (rowIdentifierType == RowIdentifierType.ID) {
            stringPair.appendInnerContent("    private final long id;");
        }
        stringPair.appendInnerContent("    private final String a;");
        stringPair.appendInnerContent("    private final String b;\n");
        if (rowIdentifierType == RowIdentifierType.ID) {
            stringPair.appendInnerContent("    public StringPair(final long id, final String a, final String b) {");
            stringPair.appendInnerContent("        this.id = id;");
        } else {
            stringPair.appendInnerContent("    public StringPair(final String a, final String b) {");
        }
        stringPair.appendInnerContent("        this.a = a;");
        stringPair.appendInnerContent("        this.b = b;");
        stringPair.appendInnerContent("    }\n");
        stringPair.appendInnerContent("    // getter");
        return stringPair;
    }

    public List<SelectItem> getAjaxSelectionTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final SelectionAjaxType type : SelectionAjaxType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getRowIdentifierTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final RowIdentifierType type : RowIdentifierType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getToolBarTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final ToolBarType type : ToolBarType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getTwoColumnWidthTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final FourthColumnWidthType type : FourthColumnWidthType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public List<SelectItem> getTableModelTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final TableModelType type : TableModelType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public void doSomethingWith(final StringPair selectedValue) {
        this.doSomethingWithRow = "I have done something with " + (selectedValue == null ? "null" : selectedValue.getA());
    }

    public SelectionAjaxType getSelectionAjaxType() {
        return selectionAjaxType;
    }

    public void setSelectionAjaxType(final SelectionAjaxType selectionAjaxType) {
        this.selectionAjaxType = selectionAjaxType;
    }

    public StringPair getSelectedValue() {
        return this.selectedValue;
    }

    public String getDoSomethingWithRow() {
        return doSomethingWithRow;
    }

    public boolean isTableCondensed() {
        return tableCondensed;
    }

    public void setTableCondensed(boolean tableCondensed) {
        this.tableCondensed = tableCondensed;
    }

    public boolean isTableBordered() {
        return tableBordered;
    }

    public void setTableBordered(boolean tableBordered) {
        this.tableBordered = tableBordered;
    }

    public boolean isTableStriped() {
        return tableStriped;
    }

    public void setTableStriped(boolean tableStriped) {
        this.tableStriped = tableStriped;
    }

    public boolean isShowRefreshButton() {
        return showRefreshButton;
    }

    public void setShowRefreshButton(boolean showRefreshButton) {
        this.showRefreshButton = showRefreshButton;
    }

    public boolean isShowToggleColumnButton() {
        return showToggleColumnButton;
    }

    public void setShowToggleColumnButton(boolean showToggleColumnButton) {
        this.showToggleColumnButton = showToggleColumnButton;
    }

    public FourthColumnWidthType getFourthColumnWidthType() {
        return fourthColumnWidthType;
    }

    public void setFourthColumnWidthType(FourthColumnWidthType fourthColumnWidthType) {
        this.fourthColumnWidthType = fourthColumnWidthType;

        switch (this.fourthColumnWidthType) {
            case NONE:
                colWidthColumn1 = null;
                colWidthColumn2 = null;
                colWidthColumn3 = null;
                colWidthColumn4 = null;
                break;
            case PERCENT:
                colWidthColumn1 = "10%";
                colWidthColumn2 = "75%";
                colWidthColumn3 = "10%";
                colWidthColumn4 = "5%";
                break;
            case PX:
                colWidthColumn1 = "50px";
                colWidthColumn2 = "30px";
                colWidthColumn3 = "10px";
                colWidthColumn4 = "10px";
                break;
            case RELATIVE:
                colWidthColumn1 = "5*";
                colWidthColumn2 = "1*";
                colWidthColumn3 = "7*";
                colWidthColumn3 = "1*";
                break;
        }
    }

    public String getColWidthColumn1() {
        return colWidthColumn1;
    }

    public String getColWidthColumn2() {
        return colWidthColumn2;
    }

    public String getColWidthColumn3() {
        return colWidthColumn3;
    }

    public String getColWidthColumn4() {
        return colWidthColumn4;
    }

    public DefaultTableModel getTableModel() {
        if (tableModelType == TableModelType.DEFAULT_MODEL) {
            return tableModel;
        }

        return null;
    }

    public TableModelType getTableModelType() {
        return tableModelType;
    }

    public void setTableModelType(TableModelType tableModelType) {
        this.tableModelType = tableModelType;
    }

    public ToolBarType getToolBarType() {
        return toolBarType;
    }

    public void setToolBarType(ToolBarType toolBarType) {
        this.toolBarType = toolBarType;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getRowClass() {
        return toolBarType == ToolBarType.CLIENT_FILTER ? "filterable-item" : null;
    }

    public boolean isAjaxDisableRenderRegionsOnRequest() {
        return ajaxDisableRenderRegionsOnRequest;
    }

    public void setAjaxDisableRenderRegionsOnRequest(boolean ajaxDisableRenderRegionsOnRequest) {
        this.ajaxDisableRenderRegionsOnRequest = ajaxDisableRenderRegionsOnRequest;
    }

    public String getRowIdentifierProperty() {
        return rowIdentifierProperty;
    }

    public RowIdentifierType getRowIdentifierType() {
        return rowIdentifierType;
    }

    public void setRowIdentifierType(RowIdentifierType rowIdentifierType) {
        this.rowIdentifierType = rowIdentifierType;

        if (rowIdentifierType == RowIdentifierType.NONE) {
            rowIdentifierProperty = null;
        } else {
            rowIdentifierProperty = "id";
        }
    }
}

