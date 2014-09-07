package de.larmic.butterfaces.test.component;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 04.09.14.
 */
@RunWith(Arquillian.class)
public class TextComponentTest extends AbstractComponentTest {

    @FindByJQuery("a.arquillian_text")
    private WebElement textLink;

    @Test
    public void testNavigateToTextComponent() throws Exception {
        browser.get(deploymentUrl + "index.jsf");

        guardHttp(textLink).click();

        Assert.assertEquals(deploymentUrl + "text.jsf", browser.getCurrentUrl());
    }
}
