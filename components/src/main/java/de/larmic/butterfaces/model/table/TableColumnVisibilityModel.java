package de.larmic.butterfaces.model.table;

/**
 * Table model to handle column visibilities. If model is not used it is not possible to hide columns by user interaction.
 */
public interface TableColumnVisibilityModel {

    /**
     * Updates table column visibility. At all times it contains complete visibility information.
     */
    void update(final TableColumnVisibility visibility);

    /**
     * @return null if column identifier is not known, table identifier is not matching or no visibility is set.
     */
    Boolean isColumnHidden(final String tableUniqueIdentifier, final String columnUniqueIdentifier);
}
