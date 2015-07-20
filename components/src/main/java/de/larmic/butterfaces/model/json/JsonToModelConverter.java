package de.larmic.butterfaces.model.json;

import de.larmic.butterfaces.model.table.TableColumnVisibilityModel;

import java.util.ArrayList;
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
        final String[] split = json.trim().split("(?<=\\}),(?=\\{)");

        final List<String> visibleColumns = new ArrayList<>();
        final List<String> invisibleColumns = new ArrayList<>();

        for (String s : split) {
            String column = s.replace("{", "");
            column = column.replace("[", "");
            column = column.replace("}", "");
            column = column.replace("]", "");
            column = column.replace("\"", "");
            final String[] attribute = column.split(",");
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

}
