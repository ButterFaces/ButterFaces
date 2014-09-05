package de.larmic.butterfaces.test.component;

import de.larmic.butterfaces.test.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 04.09.14.
 */
@RunWith(Arquillian.class)
public class TextComponentTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return Deployments.createShowcaseDeployment();
    }

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentUrl;

    @FindByJQuery("a.arquillian_text")
    private WebElement textLink;

    @Test
    public void testNavigateToTextComponent() throws Exception {
        browser.get(deploymentUrl + "index.jsf");

        guardHttp(textLink).click();

        Assert.assertEquals(deploymentUrl + "text.jsf", browser.getCurrentUrl());
    }
}
