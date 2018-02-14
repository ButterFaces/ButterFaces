package org.butterfaces.model.table.json;


import org.butterfaces.model.table.TableColumnOrdering;
import org.butterfaces.model.table.TableColumnVisibility;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by larmic on 20.07.15.
 */
public class JsonToModelConverterTest {

    @Test
    public void testConvertTableColumnVisibiilty() throws Exception {
        final String json = "[{\"identifier\":\"column1\",\"visible\":false},{\"identifier\":\"column2\",\"visible\":true},{\"identifier\":\"column3\",\"visible\":true},{\"identifier\":\"column4\",\"visible\":true},{\"identifier\":\"column5\",\"visible\":false}]";
        final String tableIdentifier = "tableIdentifier";

        final TableColumnVisibility visibility = new JsonToModelConverter().convertTableColumnVisibility(tableIdentifier, json);

        assertThat(visibility.getTableIdentifier()).isEqualTo(tableIdentifier);
        assertThat(visibility.getInvisibleColumns())
                .contains("column1", "column5")
                .doesNotContain("column2", "column3", "column4");
        assertThat(visibility.getVisibleColumns())
                .contains("column2", "column3", "column4")
                .doesNotContain("column1", "column5");
    }

    @Test
    public void testConvertTableColumnOrdering() throws Exception {
        final String json = "[{\"identifier\":\"column1\",\"position\":0},{\"identifier\":\"column2\",\"position\":1},{\"identifier\":\"column3\",\"position\":2},{\"identifier\":\"column4\",\"position\":3},{\"identifier\":\"column5\",\"position\":4}]";
        final String tableIdentifier = "tableIdentifier";

        final TableColumnOrdering ordering = new JsonToModelConverter().convertTableColumnOrdering(tableIdentifier, json);

        assertThat(ordering.getTableIdentifier()).isEqualTo(tableIdentifier);
        assertThat(ordering.getOrderedColumnIdentifiers())
                .containsExactly("column1", "column2", "column3", "column4", "column5");
    }
}