package de.larmic.butterfaces.test.component;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;

/**
 * Created by larmic on 04.09.14.
 */
@RunWith(Arquillian.class)
public class FieldSetComponentTest extends AbstractComponentTest {

    public static final String BUTTER_COMPONENT = "arquillian_component";
    public static final String OPTION_RENDERED = "arquillian_rendered";
    public static final String OPTION_LABEL = "arquillian_label";

    @Test
    public void testNavigation() throws Exception {
        browser.get(deploymentUrl + "index.jsf");
        guardHttp(this.findWebElementByClassName("arquillian_fieldset")).click();
        Assert.assertEquals("Link does not redirect to pretty print showcase", deploymentUrl + "fieldset.jsf", browser.getCurrentUrl());

        browser.get(deploymentUrl + "index.jsf");
        this.findWebElementByClassName("arquillian_container").click();
        guardHttp(this.findWebElementByClassName("arquillian_fieldset_header")).click();
        Assert.assertEquals("Link does not redirect to pretty print showcase", deploymentUrl + "fieldset.jsf", browser.getCurrentUrl());
    }

    @Test
    @InSequence(1)
    public void testElementsExists() {
        browser.get(deploymentUrl + "fieldset.jsf");

        final WebElement component = this.findWebElementByClassName(BUTTER_COMPONENT);
        Assert.assertEquals("Wrong tag name", "fieldset", component.getTagName());

        this.findWebElementByClassName(OPTION_RENDERED);
        this.findWebElementByClassName(OPTION_LABEL);
    }

    @Test
    @InSequence(2)
    public void testRenderedOption() throws Exception {
        browser.get(deploymentUrl + "fieldset.jsf");

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
    public void testLabelOption() throws Exception {
        browser.get(deploymentUrl + "fieldset.jsf");

        final WebElement showcaseLabelOption = this.findWebElementByClassName(OPTION_LABEL);

        // clear label
        showcaseLabelOption.clear();
        guardHttp(showcaseLabelOption).submit();
        WebElement component = this.findWebElementByClassName(BUTTER_COMPONENT);
        Assert.assertTrue(component.findElements(By.tagName("legend")).isEmpty());

        guardAjax(showcaseLabelOption).sendKeys("hello");
        component = this.findWebElementByClassName(BUTTER_COMPONENT);
        WebElement legend = component.findElement(By.tagName("legend"));
        Assert.assertEquals("hello", legend.getText());

        guardAjax(showcaseLabelOption).sendKeys(" world!");
        component = this.findWebElementByClassName(BUTTER_COMPONENT);
        legend = component.findElement(By.tagName("legend"));
        Assert.assertEquals("hello world!", legend.getText());
    }
}
