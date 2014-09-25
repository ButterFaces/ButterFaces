package de.larmic.butterfaces.test.component;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 17.09.14.
 */
@RunWith(Arquillian.class)
public class ActivateLibrariesTest extends AbstractComponentTest {

    public static final String COMPONENT_PAGE = "activateLibraries.jsf";

    public static final String OUTER_COMPONENT = "arquillian_activateLibraries_selector";
    public static final String OPTION_RENDERED = "arquillian_rendered";

    @Test
    public void testNavigation() throws Exception {
        browser.get(deploymentUrl + "index.jsf");
        guardHttp(this.findWebElementByClassName("arquillian_activateLibraries")).click();
        Assert.assertEquals("Link does not redirect to glyphicon link showcase", deploymentUrl + COMPONENT_PAGE, browser.getCurrentUrl());

        browser.get(deploymentUrl + "index.jsf");
        this.findWebElementByClassName("arquillian_misc").click();
        guardHttp(this.findWebElementByClassName("arquillian_activateLibraries_header")).click();
        Assert.assertEquals("Link does not redirect to glyphicon link showcase", deploymentUrl + COMPONENT_PAGE, browser.getCurrentUrl());
    }

    @Test
    @InSequence(1)
    public void testElementsExists() {
        browser.get(deploymentUrl + COMPONENT_PAGE);

        final WebElement component = this.findWebElementByClassName(OUTER_COMPONENT);
        Assert.assertEquals("Wrong tag name", "div", component.getTagName());

        this.findWebElementByClassName(OPTION_RENDERED);
    }

    @Test
    @InSequence(2)
    public void testRenderedOption() throws Exception {
        browser.get(deploymentUrl + COMPONENT_PAGE);

        final WebElement showcaseRenderedOption = this.findWebElementByClassName(OPTION_RENDERED);

        // test component rendered
        String[] content = browser.getPageSource().split("arquillian_activateLibraries_selector");
        Assert.assertTrue(content[1].startsWith("\"><!--ButterFaces information"));

        // test component not rendered
        guardAjax(showcaseRenderedOption).click();
        content = browser.getPageSource().split("arquillian_activateLibraries_selector");
        Assert.assertFalse(content[1].startsWith("\"><!--ButterFaces information"));
    }
}
