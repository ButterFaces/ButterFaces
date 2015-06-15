package de.larmic.butterfaces.model.table;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by larmic on 11.06.15.
 */
public class DefaultTableOrderModelTest {

    private TableOrderModel model = new DefaultTableOrderModel();

    @Before
    public void setUp() throws Exception {
        model.orderColumnToPosition(null, "test1", 0);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));

        model.orderColumnToPosition(null, "test2", 0);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test2"));

        model.orderColumnToPosition(null, "test3", 0);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test3"));

        model.orderColumnToPosition(null, "test4", 0);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test4"));
    }

    @Test
    public void testOrderColumnToLeft() throws Exception {
        model.orderColumnToPosition(null, "test4", 2);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test3"));

        model.orderColumnToPosition(null, "test4", 1);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test3"));

        model.orderColumnToPosition(null, "test4", 0);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test3"));

        model.orderColumnToPosition(null, "test4", -1);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test3"));
    }

    @Test
    public void testOrderColumnToRight() throws Exception {
        model.orderColumnToPosition(null, "test1", 1);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test4"));

        model.orderColumnToPosition(null, "test1", 2);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test4"));

        model.orderColumnToPosition(null, "test1", 3);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test1"));

        model.orderColumnToPosition(null, "test1", 4);
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test1"));
    }
}