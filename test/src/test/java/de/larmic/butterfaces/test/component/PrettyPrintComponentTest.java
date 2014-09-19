package de.larmic.butterfaces.test.component;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 04.09.14.
 */
@RunWith(Arquillian.class)
public class PrettyPrintComponentTest extends AbstractComponentTest {

    public static final String BUTTER_COMPONENT_PRETTYPRINT = "arquillian_component";
    public static final String OPTION_RENDERED = "arquillian_rendered";
    public static final String OPTION_LANGUAGE = "arquillian_language";

    @Test
    public void testNavigation() throws Exception {
        browser.get(deploymentUrl + "index.jsf");
        guardHttp(this.findWebElementByClassName("arquillian_prettyprint")).click();
        Assert.assertEquals("Link does not redirect to pretty print showcase", deploymentUrl + "prettyprint.jsf", browser.getCurrentUrl());

        browser.get(deploymentUrl + "index.jsf");
        this.findWebElementByClassName("arquillian_container").click();
        guardHttp(this.findWebElementByClassName("arquillian_prettyprint_header")).click();
        Assert.assertEquals("Link does not redirect to pretty print showcase", deploymentUrl + "prettyprint.jsf", browser.getCurrentUrl());
    }

    @Test
    @InSequence(1)
    public void testElementsExists() {
        browser.get(deploymentUrl + "prettyprint.jsf");

        Assert.assertEquals("Link does not redirect to pretty print showcase", deploymentUrl + "prettyprint.jsf", browser.getCurrentUrl());

        this.findShowcaseComponentPre();
        this.findWebElementByClassName(OPTION_RENDERED);
        this.findWebElementByClassName(OPTION_LANGUAGE);
    }

    @Test
    @InSequence(2)
    public void testRenderedOption() throws Exception {
        browser.get(deploymentUrl + "prettyprint.jsf");

        final WebElement showcaseRenderedOption = this.findWebElementByClassName(OPTION_RENDERED);

        // test component not rendered
        guardAjax(showcaseRenderedOption).click();
        Assert.assertNull("Element should not be rendered but was.", findNullableWebElementByClassName(BUTTER_COMPONENT_PRETTYPRINT));

        // test render component again
        guardAjax(showcaseRenderedOption).click();
        Assert.assertNotNull("Element should be rendered but was not.", findNullableWebElementByClassName(BUTTER_COMPONENT_PRETTYPRINT));
    }

    @Test
    @InSequence(3)
    public void testLanguageOption() throws Exception {
        browser.get(deploymentUrl + "prettyprint.jsf");

        final Select language = new Select(this.findWebElementByClassName(OPTION_LANGUAGE));
        language.selectByIndex(0);
        assertStyleClass("lang-html");

        language.selectByIndex(1);
        assertStyleClass("lang-xml");

        language.selectByIndex(2);
        assertStyleClass("lang-java");
    }

    private void assertStyleClass(String expectedLanguage) {
        final String classes = this.findShowcaseComponentPre().getAttribute("class");
        Assert.assertTrue(classes.contains("prettyprint"));
        Assert.assertTrue(classes.contains(expectedLanguage));
    }

    private WebElement findShowcaseComponentPre() {
        final WebElement componentContainer = this.findWebElementByClassName(BUTTER_COMPONENT_PRETTYPRINT);
        final List<WebElement> pres = componentContainer.findElements(By.tagName("pre"));

        Assert.assertEquals("Could not find pre tag in showcase component.", 1, pres.size());

        return pres.get(0);
    }

}
