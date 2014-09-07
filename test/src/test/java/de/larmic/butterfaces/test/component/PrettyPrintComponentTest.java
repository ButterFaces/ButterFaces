package de.larmic.butterfaces.test.component;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 04.09.14.
 */
@RunWith(Arquillian.class)
public class PrettyPrintComponentTest extends AbstractComponentTest {

    public static final String BUTTER_COMPONENT_PRETTYPRINT = "butter-component-prettyprint";

    @FindByJQuery("a.arquillian_prettyprint")
    private WebElement prettyPrintLink;
    @Test
    @InSequence(1)
    public void testElementsExists() {
        browser.get(deploymentUrl + "index.jsf");

        guardHttp(prettyPrintLink).click();

        Assert.assertEquals("Link does not redirect to pretty print showcase", deploymentUrl + "prettyprint.jsf", browser.getCurrentUrl());

        this.findShowcaseComponentPre();
        this.findWebElementByClassName("arquillian_rendered");
        this.findWebElementByClassName("arquillian_language");
    }

    @Test
    @InSequence(2)
    @Ignore
    public void testRenderedOption() throws Exception {
        browser.get(deploymentUrl + "prettyprint.jsf");

        final WebElement showcaseRenderedOption = this.findWebElementByClassName("arquillian_rendered");

        // test component not rendered
        guardAjax(showcaseRenderedOption).click();
        Assert.assertNull("Element should not be rendered but was.", findNullableWebElementByClassName(BUTTER_COMPONENT_PRETTYPRINT));

        // test render component again
        guardAjax(showcaseRenderedOption).click();
        Assert.assertNotNull("Element should be rendered but was not.", findNullableWebElementByClassName(BUTTER_COMPONENT_PRETTYPRINT));
    }

    private WebElement findShowcaseComponentPre() {
        final WebElement componentContainer = this.findWebElementByClassName(BUTTER_COMPONENT_PRETTYPRINT);
        final List<WebElement> pres = componentContainer.findElements(By.tagName("pre"));

        Assert.assertEquals("Could not find pre tag in showcase component.", 1, pres.size());

        return pres.get(0);
    }

}
