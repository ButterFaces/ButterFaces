package de.larmic.butterfaces.model.table;

import de.larmic.butterfaces.model.json.TableColumnVisibility;

/**
 * Created by larmic on 03.12.14.
 */
public interface TableColumnVisibilityModel {

    void update(final TableColumnVisibility visibility);

    /**
     * @return null if column id is not known.
     */
    Boolean isColumnHidden(final String tableUniqueIdentifier, final String columnUniqueIdentifier);
}
