package org.butterfaces.model.table;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by larmic on 11.06.15.
 */
class DefaultTableOrderingModelTest {

    @Test
    void testGetOrderPosition() {
        final DefaultTableOrderingModel model = new DefaultTableOrderingModel();

        assertThat(model.getOrderPosition("dtoIsNull", "dtoIsNull")).isNull();

        model.update(new TableColumnOrdering("tableIdentifier", Arrays.asList("column1", "column2", "column3", "column4")));

        assertThat(model.getOrderPosition("tableIdentifierIsNotMatching", "column1")).isNull();
        assertThat(model.getOrderPosition("tableIdentifier", "columnIdentifierIsNotMatching")).isNull();

        assertThat(model.getOrderPosition("tableIdentifier", "column1")).isEqualTo(new Integer(0));
        assertThat(model.getOrderPosition("tableIdentifier", "column2")).isEqualTo(new Integer(1));
        assertThat(model.getOrderPosition("tableIdentifier", "column3")).isEqualTo(new Integer(2));
        assertThat(model.getOrderPosition("tableIdentifier", "column4")).isEqualTo(new Integer(3));
    }
}