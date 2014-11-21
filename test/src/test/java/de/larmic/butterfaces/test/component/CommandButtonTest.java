package de.larmic.butterfaces.test.component;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 17.09.14.
 */
@RunWith(Arquillian.class)
public class CommandButtonTest extends AbstractComponentTest {

    public static final String COMPONENT_PAGE = "commandLink.jsf";

    public static final String BUTTER_COMPONENT = "arquillian_component";
    public static final String CLICKS_SPAN = "arquillian_component_clicks";
    public static final String OPTION_RENDERED = "arquillian_rendered";
    public static final String OPTION_VALUE = "arquillian_value";
    public static final String OPTION_GLYPHICON = "arquillian_glyphicon";

    @Test
    public void testNavigation() throws Exception {
        browser.get(deploymentUrl + "index.jsf");
        guardHttp(this.findWebElementByClassName("arquillian_link")).click();
        Assert.assertEquals("Link does not redirect to glyphicon link showcase", deploymentUrl + COMPONENT_PAGE, browser.getCurrentUrl());

        browser.get(deploymentUrl + "index.jsf");
        this.findWebElementByClassName("arquillian_action").click();
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
        this.findWebElementByClassName(OPTION_GLYPHICON);
        this.findWebElementByClassName(CLICKS_SPAN);
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
    @Ignore("Test failes on maven build")
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
    @InSequence(4)
    public void testGlyphiconOption() throws Exception {
        browser.get(deploymentUrl + COMPONENT_PAGE);

        final Select glyphicon = new Select(this.findWebElementByClassName(OPTION_GLYPHICON));

        guardAjax(glyphicon).selectByIndex(0);
        WebElement component = this.findWebElementByClassName(BUTTER_COMPONENT);
        List<WebElement> spans = component.findElements(By.cssSelector("span.butter-component-glyphicon"));
        Assert.assertEquals("Find span but span should not been rendered.", 0, spans.size());

        guardAjax(glyphicon).selectByIndex(1);
        spans = component.findElements(By.cssSelector("span.butter-component-glyphicon"));
        Assert.assertEquals("Could not find span tag in showcase component.", 1, spans.size());
        Assert.assertTrue(spans.get(0).getAttribute("class").contains("glyphicon"));
        Assert.assertTrue(spans.get(0).getAttribute("class").contains("glyphicon-thumbs-up"));
        Assert.assertTrue(spans.get(0).getAttribute("class").contains("glyphicon-lg"));
        Assert.assertTrue(spans.get(0).getAttribute("class").contains("butter-component-glyphicon"));

        guardAjax(glyphicon).selectByIndex(2);
        spans = component.findElements(By.cssSelector("span.butter-component-glyphicon"));
        Assert.assertEquals("Could not find span tag in showcase component.", 1, spans.size());
        Assert.assertTrue(spans.get(0).getAttribute("class").contains("fa"));
        Assert.assertTrue(spans.get(0).getAttribute("class").contains("fa-language"));
        Assert.assertTrue(spans.get(0).getAttribute("class").contains("fa-lg"));
        Assert.assertTrue(spans.get(0).getAttribute("class").contains("butter-component-glyphicon"));
    }

    @Test
    @InSequence(5)
    public void testClick() throws Exception {
        browser.get(deploymentUrl + COMPONENT_PAGE);

        Assert.assertEquals("0", this.findWebElementByClassName(CLICKS_SPAN).getText());

        final WebElement link = this.findWebElementByClassName(BUTTER_COMPONENT);

        // disable feature ajaxDisableLinkOnRequest becauser otherwise clicking link will be waiting severel seconds (see showcase)
        guardAjax(this.findWebElementByClassName("arquillian_ajaxDisableLinkOnRequest")).click();

        guardAjax(link).click();
        Assert.assertEquals("1 clicks", this.findWebElementByClassName(CLICKS_SPAN).getText());

        guardAjax(link).click();
        Assert.assertEquals("2 clicks", this.findWebElementByClassName(CLICKS_SPAN).getText());
    }
}
