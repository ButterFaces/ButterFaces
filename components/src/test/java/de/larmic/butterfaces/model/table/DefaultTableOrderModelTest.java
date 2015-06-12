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
        model.orderColumnToLeft(null, "test1");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));

        model.orderColumnToLeft(null, "test2");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test2"));

        model.orderColumnToLeft(null, "test3");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test3"));

        model.orderColumnToLeft(null, "test4");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test4"));
    }

    @Test
    public void testOrderColumnToLeft() throws Exception {
        model.orderColumnToLeft(null, "test4");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test3"));

        model.orderColumnToLeft(null, "test4");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test3"));

        model.orderColumnToLeft(null, "test4");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test3"));

        model.orderColumnToLeft(null, "test4");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test3"));
    }

    @Test
    public void testOrderColumnToRight() throws Exception {
        model.orderColumnToRight(null, "test1");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test4"));

        model.orderColumnToRight(null, "test1");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test1"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test4"));

        model.orderColumnToRight(null, "test1");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test1"));

        model.orderColumnToRight(null, "test1");
        Assert.assertEquals(new Integer(0), model.getOrderPosition(null, "test2"));
        Assert.assertEquals(new Integer(1), model.getOrderPosition(null, "test3"));
        Assert.assertEquals(new Integer(2), model.getOrderPosition(null, "test4"));
        Assert.assertEquals(new Integer(3), model.getOrderPosition(null, "test1"));
    }
}