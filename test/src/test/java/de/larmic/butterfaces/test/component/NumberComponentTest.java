package de.larmic.butterfaces.test.component;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 17.09.14.
 */
@RunWith(Arquillian.class)
public class NumberComponentTest extends AbstractComponentTest {

    public static final String COMPONENT_PAGE = "number.jsf";

    public static final String ARQUILLIAN_CONTAINER = "arquillian_component";
    public static final String OPTION_RENDERED = "arquillian_rendered";

    @Test
    public void testNavigation() throws Exception {
        browser.get(deploymentUrl + "index.jsf");
        guardHttp(this.findWebElementByClassName("arquillian_number")).click();
        Assert.assertEquals("Link does not redirect to number component showcase", deploymentUrl + COMPONENT_PAGE, browser.getCurrentUrl());

        browser.get(deploymentUrl + "index.jsf");
        this.findWebElementByClassName("arquillian_components").click();
        guardHttp(this.findWebElementByClassName("arquillian_number_header")).click();
        Assert.assertEquals("Link does not redirect to number component showcase", deploymentUrl + COMPONENT_PAGE, browser.getCurrentUrl());
    }

    @Test
    @InSequence(1)
    public void testElementsExists() {
        browser.get(deploymentUrl + COMPONENT_PAGE);

        final WebElement outerComponent = this.findWebElementByClassName(ARQUILLIAN_CONTAINER);
        Assert.assertEquals("Wrong tag name", "div", outerComponent.getTagName());
        final WebElement component = outerComponent.findElement(By.className("butter-component"));
        Assert.assertEquals("Wrong tag name", "div", component.getTagName());
        Assert.assertEquals("Wrong tag name", "button", component.findElement(By.className("bootstrap-touchspin-up")).getTagName());
        Assert.assertEquals("Wrong tag name", "button", component.findElement(By.className("bootstrap-touchspin-down")).getTagName());

        this.findWebElementByClassName(OPTION_RENDERED);
    }

    @Test
    @InSequence(2)
    public void testRenderedOption() throws Exception {
        browser.get(deploymentUrl + COMPONENT_PAGE);

        final WebElement showcaseRenderedOption = this.findWebElementByClassName(OPTION_RENDERED);

        // test component not rendered
        guardAjax(showcaseRenderedOption).click();
        Assert.assertNull("Element should not be rendered but was.", findNullableButterComponent());

        // test render component again
        guardAjax(showcaseRenderedOption).click();
        Assert.assertNotNull("Element should be rendered but was not.", findNullableButterComponent());
    }

    private WebElement findNullableButterComponent() {
        try {
            final WebElement outerComponent = this.findWebElementByClassName(ARQUILLIAN_CONTAINER);
            return outerComponent.findElement(By.className("butter-component"));
        } catch (NoSuchElementException e) {
        }

        return null;

    }
}
