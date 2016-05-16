/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic.table.cache;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple cache for {@link UIColumn}s.
 *
 * @author Lars Michaelis
 */
public class TableColumnCache {

    public static final String KEY = TableColumnCache.class.getName();

    private final List<UIColumn> cachedColumns;

    public TableColumnCache(UIComponent table) {
        cachedColumns = buildCache(table);
    }

    public List<UIColumn> getCachedColumns() {
        return cachedColumns;
    }

    /**
     * <p>Return an Iterator over the <code>UIColumn</code> children of the
     * specified <code>UIData</code> that have a <code>rendered</code> property
     * of <code>true</code>.</p>
     *
     * @param table the table from which to extract children
     * @return the List of all UIColumn children
     */
    private static List<UIColumn> buildCache(UIComponent table) {
        if (table instanceof UIData) {
            final int childCount = table.getChildCount();
            if (childCount > 0) {
                final List<UIColumn> results = new ArrayList<>(childCount);
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
            final Object value = table.getAttributes().get("cachedColumns");
            if ((value != null) && (value instanceof Integer)) {
                count = ((Integer) value);
            } else {
                count = 2;
            }
            if (count < 1) {
                count = 1;
            }
            final List<UIColumn> result = new ArrayList<UIColumn>(count);
            for (int i = 0; i < count; i++) {
                result.add(new UIColumn());
            }
            return result;
        }

    }

}
