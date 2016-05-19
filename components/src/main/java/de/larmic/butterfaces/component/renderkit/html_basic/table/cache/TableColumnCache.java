/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.renderkit.html_basic.table.cache;

import de.larmic.butterfaces.component.html.table.HtmlColumn;
import de.larmic.butterfaces.component.html.table.HtmlTable;
import de.larmic.butterfaces.model.table.json.Ordering;
import de.larmic.butterfaces.model.table.TableColumnOrdering;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple cache for {@link UIColumn}s.
 *
 * @author Lars Michaelis
 */
public class TableColumnCache {

    public static final String KEY = TableColumnCache.class.getName();

    private final List<HtmlColumn> cachedColumns;

    public TableColumnCache(HtmlTable table) {
        cachedColumns = buildCache(table);
        orderColumns(table, cachedColumns);
    }

    public List<HtmlColumn> getCachedColumns() {
        return cachedColumns;
    }

    private static void orderColumns(final HtmlTable table, final List<HtmlColumn> cachedColumns) {
        // sort cachedColumns by model if necessary
        if (table.getTableOrderingModel() != null) {
            final List<HtmlColumn> notOrderedByModelColumnIdentifiers = new ArrayList<>();
            final List<Ordering> existingOrderings = new ArrayList<>();

            for (HtmlColumn cachedColumn : cachedColumns) {
                final Integer position = table.getTableOrderingModel().getOrderPosition(table.getModelUniqueIdentifier(), cachedColumn.getModelUniqueIdentifier());
                if (position == null) {
                    notOrderedByModelColumnIdentifiers.add(cachedColumn);
                } else {
                    existingOrderings.add(new Ordering(cachedColumn.getModelUniqueIdentifier(), position));
                }
            }

            // in case of not ordered cachedColumns update table model
            if (!notOrderedByModelColumnIdentifiers.isEmpty()) {
                // order already existing column orderings
                Ordering.sort(existingOrderings);

                final List<String> orderings = new ArrayList<>();
                for (Ordering existingOrdering : existingOrderings) {
                    orderings.add(existingOrdering.getIdentifier());
                }
                for (HtmlColumn notOrderedByModelColumnIdentifier : notOrderedByModelColumnIdentifiers) {
                    orderings.add(notOrderedByModelColumnIdentifier.getModelUniqueIdentifier());
                }

                // update table model to sync model and
                final TableColumnOrdering ordering = new TableColumnOrdering(table.getModelUniqueIdentifier(), orderings);
                table.getTableOrderingModel().update(ordering);
            }

            // sort cachedColumns by table model. Every column should be found.
            Collections.sort(cachedColumns, new Comparator<HtmlColumn>() {
                @Override
                public int compare(HtmlColumn o1, HtmlColumn o2) {
                    if (table.getTableOrderingModel() != null) {
                        final Integer orderPosition = table.getTableOrderingModel().getOrderPosition(table.getModelUniqueIdentifier(), o1.getModelUniqueIdentifier());
                        final Integer o2OrderPosition = table.getTableOrderingModel().getOrderPosition(table.getModelUniqueIdentifier(), o2.getModelUniqueIdentifier());

                        if (orderPosition != null && o2OrderPosition != null) {
                            return orderPosition.compareTo(o2OrderPosition);
                        }
                    }
                    return 0;
                }
            });
        }
    }

    /**
     * <p>Return an Iterator over the <code>UIColumn</code> children of the
     * specified <code>UIData</code> that have a <code>rendered</code> property
     * of <code>true</code>.</p>
     *
     * @param table the table from which to extract children
     * @return the List of all UIColumn children
     */
    private static List<HtmlColumn> buildCache(UIComponent table) {
        if (table instanceof UIData) {
            final int childCount = table.getChildCount();
            if (childCount > 0) {
                final List<HtmlColumn> results = new ArrayList<>(childCount);
                for (UIComponent kid : table.getChildren()) {
                    if ((kid instanceof UIColumn) && kid.isRendered()) {
                        results.add((HtmlColumn) kid);
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
            final List<HtmlColumn> result = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                result.add(new HtmlColumn());
            }
            return result;
        }

    }

}
