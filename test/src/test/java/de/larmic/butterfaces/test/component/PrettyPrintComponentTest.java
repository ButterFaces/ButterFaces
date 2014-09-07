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
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 04.09.14.
 */
@RunWith(Arquillian.class)
public class PrettyPrintComponentTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return Deployments.createShowcaseDeployment();
    }

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentUrl;

    @FindByJQuery("a.arquillian_prettyprint")
    private WebElement prettyPrintLink;

    @Test
    public void testNavigateToTextComponent() throws Exception {
        browser.get(deploymentUrl + "index.jsf");

        guardHttp(prettyPrintLink).click();

        Assert.assertEquals("Link does not redirect to pretty print showcase", deploymentUrl + "prettyprint.jsf", browser.getCurrentUrl());

        final WebElement showcaseComponentPre = this.findShowcaseComponentPre();
        final WebElement showcaseRenderedOption = this.findWebElementByClassName("arquillian_rendered");
        final WebElement showcaseLanguageOption = this.findWebElementByClassName("arquillian_language");
    }

    private WebElement findShowcaseComponentPre() {
        final WebElement componentContainer = this.findWebElementByClassName("butter-component-prettyprint");
        final List<WebElement> pres = componentContainer.findElements(By.tagName("pre"));

        Assert.assertEquals("Could not find pre tag in showcase component.", 1, pres.size());

        return pres.get(0);
    }

    private WebElement findWebElementByClassName(final String className) {
        try {
            final WebElement webElement = browser.findElement(By.className(className));
            return webElement;
        } catch (NoSuchElementException e) {
            Assert.fail(e.toString());
        }

        return null;

    }
}
