package de.larmic.butterfaces.model.table;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by larmic on 20.07.15.
 */
public class DefaultTableColumnVisibilityModelTest {

    @Test
    public void testIsColumnHidden() throws Exception {
        final DefaultTableColumnVisibilityModel model = new DefaultTableColumnVisibilityModel();

        Assert.assertNull(model.isColumnHidden("dtoIsNull", "dtoIsNull"));

        model.update(new TableColumnVisibility("tableIdentifier", Arrays.asList("visible1", "visible2"), Arrays.asList("invisible1", "invisible2")));

        Assert.assertNull(model.isColumnHidden("tableIdentifierIsNotMatching", "visible1"));
        Assert.assertNull(model.isColumnHidden("tableIdentifier", "columnIdentifierIsNotMatching"));
        Assert.assertFalse(model.isColumnHidden("tableIdentifier", "visible1"));
        Assert.assertFalse(model.isColumnHidden("tableIdentifier", "visible2"));
        Assert.assertTrue(model.isColumnHidden("tableIdentifier", "invisible1"));
        Assert.assertTrue(model.isColumnHidden("tableIdentifier", "invisible2"));
    }
}