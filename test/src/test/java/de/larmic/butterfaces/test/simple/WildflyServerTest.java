package de.larmic.butterfaces.test.simple;

import de.larmic.butterfaces.test.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;

/**
 * Simple test to start wildfly server by using arquillian.
 */
@RunWith(Arquillian.class)
public class WildflyServerTest {

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
    @FindByJQuery("a.arquillian_textarea")
    private WebElement textAreaLink;
    @FindByJQuery("a.arquillian_secret")
    private WebElement secretink;
    @FindByJQuery("a.arquillian_number")
    private WebElement numberLink;
    @FindByJQuery("a.arquillian_checkbox")
    private WebElement checkboxLink;
    @FindByJQuery("a.arquillian_combobox")
    private WebElement comboboxLink;
    @FindByJQuery("a.arquillian_radiobox")
    private WebElement radioboxLink;
    @FindByJQuery("a.arquillian_fieldset")
    private WebElement fieldsetLink;
    @FindByJQuery("a.arquillian_prettyprint")
    private WebElement prettyPrintLink;

    /**
     * Run test endless for manually test.
     */
    @Test
    @Ignore
    public void runWildflyEndless() {
        System.out.println(deploymentUrl);
        browser.get(deploymentUrl + "index.jsf");

        //guardHttp(textLink).click();

        while (true) {

        }
    }

    /**
     * Tests finding all component links on butterfaces welcome page.
     */
    @Test
    public void testWelcomPage() throws Exception {
        browser.get(deploymentUrl + "index.jsf");

        Assert.assertEquals("a", textLink.getTagName());
        Assert.assertEquals("a", textAreaLink.getTagName());
        Assert.assertEquals("a", secretink.getTagName());
        Assert.assertEquals("a", numberLink.getTagName());
        Assert.assertEquals("a", checkboxLink.getTagName());
        Assert.assertEquals("a", comboboxLink.getTagName());
        Assert.assertEquals("a", radioboxLink.getTagName());
        Assert.assertEquals("a", fieldsetLink.getTagName());
        Assert.assertEquals("a", prettyPrintLink.getTagName());
    }
}
