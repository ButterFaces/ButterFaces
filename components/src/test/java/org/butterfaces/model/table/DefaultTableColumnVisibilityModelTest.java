package org.butterfaces.model.table;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by larmic on 20.07.15.
 */
class DefaultTableColumnVisibilityModelTest {

    @Test
    void testIsColumnHidden() {
        final DefaultTableColumnVisibilityModel model = new DefaultTableColumnVisibilityModel();

        assertThat(model.isColumnHidden("dtoIsNull", "dtoIsNull")).isNull();

        model.update(new TableColumnVisibility("tableIdentifier", Arrays.asList("visible1", "visible2"), Arrays.asList("invisible1", "invisible2")));

        assertThat(model.isColumnHidden("tableIdentifierIsNotMatching", "visible1")).isNull();
        assertThat(model.isColumnHidden("tableIdentifier", "columnIdentifierIsNotMatching")).isNull();
        assertThat(model.isColumnHidden("tableIdentifier", "visible1")).isFalse();
        assertThat(model.isColumnHidden("tableIdentifier", "visible2")).isFalse();
        assertThat(model.isColumnHidden("tableIdentifier", "invisible1")).isTrue();
        assertThat(model.isColumnHidden("tableIdentifier", "invisible2")).isTrue();
    }
}