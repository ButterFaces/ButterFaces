package de.larmic.butterfaces.model.table;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by larmic on 11.06.15.
 */
public class DefaultTableOrderingModelTest {

    @Test
    public void testGetOrderPosition() throws Exception {
        final DefaultTableOrderingModel model = new DefaultTableOrderingModel();

        Assert.assertNull(model.getOrderPosition("dtoIsNull", "dtoIsNull"));

        model.update(new TableColumnOrdering("tableIdentifier", Arrays.asList("column1", "column2", "column3", "column4")));

        Assert.assertNull(model.getOrderPosition("tableIdentifierIsNotMatching", "column1"));
        Assert.assertNull(model.getOrderPosition("tableIdentifier", "columnIdentifierIsNotMatching"));

        Assert.assertEquals(new Integer(0), model.getOrderPosition("tableIdentifier", "column1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition("tableIdentifier", "column2"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition("tableIdentifier", "column3"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition("tableIdentifier", "column4"));
    }
}