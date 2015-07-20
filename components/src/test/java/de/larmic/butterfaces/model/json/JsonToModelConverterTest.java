package de.larmic.butterfaces.model.json;


import de.larmic.butterfaces.model.table.TableColumnOrdering;
import de.larmic.butterfaces.model.table.TableColumnVisibility;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by larmic on 20.07.15.
 */
public class JsonToModelConverterTest {

    @Test
    public void testConvertTableColumnVisibiilty() throws Exception {
        final String json = "[{\"identifier\":\"column1\",\"visible\":false},{\"identifier\":\"column2\",\"visible\":true},{\"identifier\":\"column3\",\"visible\":true},{\"identifier\":\"column4\",\"visible\":true},{\"identifier\":\"column5\",\"visible\":false}]";
        final String tableIdentifier = "tableIdentifier";

        final TableColumnVisibility visibility = new JsonToModelConverter().convertTableColumnVisibility(tableIdentifier, json);

        Assert.assertEquals(tableIdentifier, visibility.getTableIdentifier());
        Assert.assertTrue(visibility.getInvisibleColumns().contains("column1"));
        Assert.assertTrue(visibility.getInvisibleColumns().contains("column5"));
        Assert.assertTrue(visibility.getVisibleColumns().contains("column2"));
        Assert.assertTrue(visibility.getVisibleColumns().contains("column3"));
        Assert.assertTrue(visibility.getVisibleColumns().contains("column4"));
    }

    @Test
    public void testConvertTableColumnOrdering() throws Exception {
        final String json = "[{\"identifier\":\"column1\",\"position\":0},{\"identifier\":\"column2\",\"position\":1},{\"identifier\":\"column3\",\"position\":2},{\"identifier\":\"column4\",\"position\":3},{\"identifier\":\"column5\",\"position\":4}]";
        final String tableIdentifier = "tableIdentifier";

        final TableColumnOrdering ordering = new JsonToModelConverter().convertTableColumnOrdering(tableIdentifier, json);

        Assert.assertEquals(tableIdentifier, ordering.getTableIdentifier());
        Assert.assertEquals("column1", ordering.getOrderedColumnIdentifiers().get(0));
        Assert.assertEquals("column2", ordering.getOrderedColumnIdentifiers().get(1));
        Assert.assertEquals("column3", ordering.getOrderedColumnIdentifiers().get(2));
        Assert.assertEquals("column4", ordering.getOrderedColumnIdentifiers().get(3));
        Assert.assertEquals("column5", ordering.getOrderedColumnIdentifiers().get(4));
    }
}