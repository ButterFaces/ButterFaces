package de.larmic.butterfaces.test.component;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 04.09.14.
 */
@RunWith(Arquillian.class)
public class PrettyPrintComponentTest extends AbstractComponentTest {

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

}
