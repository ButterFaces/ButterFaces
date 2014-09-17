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
public class GlyphiconCommandButtonTest extends AbstractComponentTest {

    public static final String COMPONENT_PAGE = "commandLink.jsf";

    public static final String BUTTER_COMPONENT = "arquillian_component";
    public static final String OPTION_RENDERED = "arquillian_rendered";
    public static final String OPTION_VALUE = "arquillian_value";

    @Test
    public void testNavigation() throws Exception {
        browser.get(deploymentUrl + "index.jsf");
        guardHttp(this.findWebElementByClassName("arquillian_link")).click();
        Assert.assertEquals("Link does not redirect to glyphicon link showcase", deploymentUrl + COMPONENT_PAGE, browser.getCurrentUrl());

        browser.get(deploymentUrl + "index.jsf");
        guardHttp(this.findWebElementByClassName("arquillian_link_header")).click();
        Assert.assertEquals("Link does not redirect to glyphicon link showcase", deploymentUrl + COMPONENT_PAGE, browser.getCurrentUrl());
    }

    @Test
    @InSequence(1)
    public void testElementsExists() {
        browser.get(deploymentUrl + COMPONENT_PAGE);

        final WebElement component = this.findWebElementByClassName(BUTTER_COMPONENT);
        Assert.assertEquals("Wrong tag name", "a", component.getTagName());

        this.findWebElementByClassName(OPTION_RENDERED);
        this.findWebElementByClassName(OPTION_VALUE);
    }

    @Test
    @InSequence(2)
    public void testRenderedOption() throws Exception {
        browser.get(deploymentUrl + COMPONENT_PAGE);

        final WebElement showcaseRenderedOption = this.findWebElementByClassName(OPTION_RENDERED);

        // test component not rendered
        guardAjax(showcaseRenderedOption).click();
        Assert.assertNull("Element should not be rendered but was.", findNullableWebElementByClassName(BUTTER_COMPONENT));

        // test render component again
        guardAjax(showcaseRenderedOption).click();
        Assert.assertNotNull("Element should be rendered but was not.", findNullableWebElementByClassName(BUTTER_COMPONENT));
    }

    @Test
    @InSequence(3)
    public void testValueOption() throws Exception {
        browser.get(deploymentUrl + COMPONENT_PAGE);

        final WebElement showcaseValueOption = this.findWebElementByClassName(OPTION_VALUE);

        // clear label
        showcaseValueOption.clear();
        guardHttp(showcaseValueOption).submit();
        WebElement component = this.findWebElementByClassName(BUTTER_COMPONENT);
        Assert.assertTrue(component.getText().isEmpty());

        guardAjax(showcaseValueOption).sendKeys("hello");
        component = this.findWebElementByClassName(BUTTER_COMPONENT);
        Assert.assertEquals("hello", component.getText());

        guardAjax(showcaseValueOption).sendKeys(" world!");
        component = this.findWebElementByClassName(BUTTER_COMPONENT);
        Assert.assertEquals("hello world!", component.getText());
    }

    @Test
    @InSequence(3)
    public void testGlyphiconOption() throws Exception {

    }
}
