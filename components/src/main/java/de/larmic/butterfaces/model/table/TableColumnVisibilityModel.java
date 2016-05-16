package de.larmic.butterfaces.model.table;

/**
 * Table model to handle column visibilities. If model is not used it is not possible to hide cachedColumns by user interaction.
 */
public interface TableColumnVisibilityModel {

    /**
     * Updates table column visibility. At all times it contains complete visibility information.
     *
     * @param visibility dto
     */
    void update(final TableColumnVisibility visibility);

    /**
     * @param tableUniqueIdentifier  table unique identifier (component client if if unique identifier is empty)
     * @param columnUniqueIdentifier column unique identifier (component client if if unique identifier is empty)
     * @return null if column identifier is not known, table identifier is not matching or no visibility is set.
     */
    Boolean isColumnHidden(final String tableUniqueIdentifier, final String columnUniqueIdentifier);
}
