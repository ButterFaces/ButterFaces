package de.larmic.butterfaces.model.table;

/**
 * Table model to handle column visibilities. If model is not used it is not possible to hide columns by user interaction.
 */
public interface TableColumnVisibilityModel {

    void update(final TableColumnVisibility visibility);

    /**
     * @return null if column id is not known.
     */
    Boolean isColumnHidden(final String tableUniqueIdentifier, final String columnUniqueIdentifier);
}
