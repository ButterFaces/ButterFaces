package de.larmic.butterfaces.model.table.json;

import de.larmic.butterfaces.model.table.TableColumnOrdering;
import de.larmic.butterfaces.model.table.TableColumnOrderingModel;
import de.larmic.butterfaces.model.table.TableColumnVisibility;
import de.larmic.butterfaces.model.table.TableColumnVisibilityModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This is a very simple JSON converter for converting javascript generated JSON strings to model dto.
 */
public class JsonToModelConverter {

    /**
     * Converts given json string to {@link TableColumnVisibility} used by {@link TableColumnVisibilityModel}.
     *
     * @param tableIdentifier an application unique table identifier.
     * @param json            string to convert to {@link TableColumnVisibility}.
     * @return the converted {@link TableColumnVisibility}.
     */
    public TableColumnVisibility convertTableColumnVisibility(final String tableIdentifier, final String json) {
        final String[] split = this.splitColumns(json);

        final List<String> visibleColumns = new ArrayList<>();
        final List<String> invisibleColumns = new ArrayList<>();

        for (String column : split) {
            final String[] attribute = this.splitAttributes(column);
            final String identifier = attribute[0].split(":")[1];
            final String visible = attribute[1].split(":")[1];

            if (Boolean.valueOf(visible)) {
                visibleColumns.add(identifier);
            } else {
                invisibleColumns.add(identifier);
            }
        }

        return new TableColumnVisibility(tableIdentifier, visibleColumns, invisibleColumns);
    }

    /**
     * Converts given json string to {@link TableColumnOrdering} used by {@link TableColumnOrderingModel}.
     *
     * @param tableIdentifier an application unique table identifier.
     * @param json            string to convert to {@link TableColumnOrdering}.
     * @return the converted {@link TableColumnOrdering}.
     */
    public TableColumnOrdering convertTableColumnOrdering(final String tableIdentifier, final String json) {
        final String[] split = this.splitColumns(json);

        final List<Ordering> orderings = new ArrayList<>();

        for (String column : split) {
            final String[] attribute = this.splitAttributes(column);
            final String identifier = attribute[0].split(":")[1];
            final String position = attribute[1].split(":")[1];

            orderings.add(new Ordering(identifier, Integer.valueOf(position)));
        }

        Collections.sort(orderings, new Comparator<Ordering>() {
            @Override
            public int compare(Ordering o1, Ordering o2) {
                return o1.getIndex().compareTo(o2.getIndex());
            }
        });

        final List<String> columnIdentifier = new ArrayList<>();

        for (Ordering ordering : orderings) {
            columnIdentifier.add(ordering.getIdentifier());
        }

        return new TableColumnOrdering(tableIdentifier, columnIdentifier);
    }

    private String[] splitColumns(String json) {
        return json.trim().split("(?<=\\}),(?=\\{)");
    }

    private String[] splitAttributes(final String column) {
        String cleanedColumn = column.replace("{", "");
        cleanedColumn = cleanedColumn.replace("[", "");
        cleanedColumn = cleanedColumn.replace("}", "");
        cleanedColumn = cleanedColumn.replace("]", "");
        cleanedColumn = cleanedColumn.replace("\"", "");
        return cleanedColumn.split(",");
    }
}
