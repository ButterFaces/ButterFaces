package de.larmic.butterfaces.component.renderkit.html_basic.table.cache;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by larmic on 13.05.16.
 */
public class TableMetaInfo {

    public static final String KEY = de.larmic.butterfaces.component.renderkit.html_basic.table.cache.TableMetaInfo.class.getName();

    public final List<UIColumn> columns;
    public final int columnCount;
    public int columnStyleCounter;


    // -------------------------------------------------------- Constructors


    public TableMetaInfo(UIComponent table) {
        columns = getColumns(table);
        columnCount = columns.size();
    }


    // ------------------------------------------------------ Public Methods


    /**
     * Reset the counter used to apply column styles.
     */
    public void newRow() {

        columnStyleCounter = 0;

    }


    // ----------------------------------------------------- Private Methods

    /**
     * <p>Return an Iterator over the <code>UIColumn</code> children of the
     * specified <code>UIData</code> that have a <code>rendered</code> property
     * of <code>true</code>.</p>
     *
     * @param table the table from which to extract children
     * @return the List of all UIColumn children
     */
    private static List<UIColumn> getColumns(UIComponent table) {

        if (table instanceof UIData) {
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
        } else {
            int count;
            Object value = table.getAttributes().get("columns");
            if ((value != null) && (value instanceof Integer)) {
                count = ((Integer) value);
            } else {
                count = 2;
            }
            if (count < 1) {
                count = 1;
            }
            List<UIColumn> result = new ArrayList<UIColumn>(count);
            for (int i = 0; i < count; i++) {
                result.add(new UIColumn());
            }
            return result;
        }

    }

}
