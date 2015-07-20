package de.larmic.butterfaces.model.json;


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

        final TableColumnVisibility tableColumnVisibility = new JsonToModelConverter().convertTableColumnVisibility(tableIdentifier, json);

        Assert.assertEquals(tableIdentifier, tableColumnVisibility.getTableIdentifier());
        Assert.assertTrue(tableColumnVisibility.getInvisibleColumns().contains("column1"));
        Assert.assertTrue(tableColumnVisibility.getInvisibleColumns().contains("column5"));
        Assert.assertTrue(tableColumnVisibility.getVisibleColumns().contains("column2"));
        Assert.assertTrue(tableColumnVisibility.getVisibleColumns().contains("column3"));
        Assert.assertTrue(tableColumnVisibility.getVisibleColumns().contains("column4"));
    }
}