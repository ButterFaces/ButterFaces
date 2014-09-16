package de.larmic.butterfaces.test.component;

import de.larmic.butterfaces.test.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.*;

import java.net.URL;

/**
 * Created by larmic on 07.09.14.
 */
public abstract class AbstractComponentTest {

    @Drone
    protected WebDriver browser;
    @ArquillianResource
    protected URL deploymentUrl;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return Deployments.createShowcaseDeployment();
    }

    @Before
    public void initBrowserSize() {
        browser.manage().window().setSize(new Dimension(1280, 1024));
    }

    protected WebElement findWebElementByClassName(final String className) {
        final WebElement webElement = findNullableWebElementByClassName(className);

        if (webElement == null) {
            Assert.fail("Could not find web element with class " + className);
        }

        return webElement;

    }

    protected WebElement findNullableWebElementByClassName(final String className) {
        try {
            final WebElement webElement = browser.findElement(By.className(className));
            return webElement;
        } catch (NoSuchElementException e) {
        }

        return null;

    }
}
