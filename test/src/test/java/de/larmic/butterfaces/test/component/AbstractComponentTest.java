package de.larmic.butterfaces.test.component;

import de.larmic.butterfaces.test.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    protected WebElement findWebElementByClassName(final String className) {
        try {
            final WebElement webElement = browser.findElement(By.className(className));
            return webElement;
        } catch (NoSuchElementException e) {
            Assert.fail(e.toString());
        }

        return null;

    }
}
