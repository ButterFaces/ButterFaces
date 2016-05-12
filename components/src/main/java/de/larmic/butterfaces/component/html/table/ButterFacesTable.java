/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table;

import de.larmic.butterfaces.model.table.TableColumnOrderingModel;
import de.larmic.butterfaces.model.table.TableColumnVisibilityModel;

import java.util.List;

/**
 * An interface to allow custom table implementation but using ButterFaces column and toolbar
 *
 * @author Lars Michaelis and Stephan Zerhusen
 */
public interface ButterFacesTable {

    String getClientId();
    String getModelUniqueIdentifier();
    TableColumnVisibilityModel getTableColumnVisibilityModel();
    TableColumnOrderingModel getTableOrderingModel();
    List<HtmlColumn> getCachedColumns();
    boolean isHideColumn(final HtmlColumn column);

}
