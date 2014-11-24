package de.larmic.butterfaces.component.showcase.table;

import de.larmic.butterfaces.component.showcase.AbstractShowcaseComponent;
import de.larmic.butterfaces.component.showcase.tree.SelectionAjaxType;
import de.larmic.butterfaces.event.TableSingleSelectionListener;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larmic on 11.09.14.
 */
@Named
@ViewScoped
public class TableShowcaseComponent extends AbstractShowcaseComponent implements Serializable, TableSingleSelectionListener<StringPair> {

    private final List<StringPair> stringPairs = new ArrayList<>();
    private StringPair selectedValue = null;
    private SelectionAjaxType selectionAjaxType = SelectionAjaxType.AJAX;

    public List<StringPair> getStringRows() {
        if (stringPairs.isEmpty()) {
            stringPairs.add(new StringPair("r1c1", "r1c2"));
            stringPairs.add(new StringPair("r2c1", "r2c2"));
            stringPairs.add(new StringPair("r3c1", "r3c2"));
            stringPairs.add(new StringPair("r4c1", "r4c2"));
            stringPairs.add(new StringPair("r5c1", "r5c2"));
            stringPairs.add(new StringPair("r6c1", "r6c2"));
            stringPairs.add(new StringPair("r7c1", "r7c2"));
        }
        return stringPairs;
    }

    @Override
    public void processValueChange(final StringPair data) {
        this.selectedValue = data;
    }

    public List<SelectItem> getAjaxSelectionTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final SelectionAjaxType type : SelectionAjaxType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    @Override
    protected void addJavaCode(final StringBuilder sb) {
        sb.append("package de.larmic.table,demo;\n\n");

        if (this.selectionAjaxType == SelectionAjaxType.AJAX) {
            sb.append("import de.larmic.butterfaces.event.TableSingleSelectionListener;\n");
        }
        sb.append("import javax.faces.view.ViewScoped;\n");
        sb.append("import javax.inject.Named;\n\n");

        sb.append("@ViewScoped\n");
        sb.append("@Named\n");
        if (this.selectionAjaxType == SelectionAjaxType.AJAX) {
            sb.append("public class MyBean implements Serializable, TableSingleSelectionListener {\n\n");
        } else {
            sb.append("public class MyBean implements Serializable {\n\n");
        }

        sb.append("}\n\n");

        sb.append("public class StringPair {\n\n");
        sb.append("    private final String a;\n");
        sb.append("    private final String b;\n\n");
        sb.append("    public StringPair(final String a, final String b) {\n");
        sb.append("        this.a = a;\n");
        sb.append("        this.b = b;\n");
        sb.append("    }\n\n");
        sb.append("    // getter\n\n");
        sb.append("}\n\n");
    }

    @Override
    public String getXHtml() {
        final StringBuilder sb = new StringBuilder();

        this.addXhtmlStart(sb);

        sb.append("        <b:table id=\"input\"\n");

        this.appendString("var", "rowItem", sb);
        this.appendString("value", "#{myBean.value}", sb);
        this.appendBoolean("rendered", this.isRendered(), sb, true);

        sb.append("        </b:table>");

        this.addXhtmlEnd(sb);

        return sb.toString();
    }

    @Override
    protected String getEmptyDistanceString() {
        return "                 ";
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
}

