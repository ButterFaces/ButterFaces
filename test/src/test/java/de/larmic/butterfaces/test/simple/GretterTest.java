package de.larmic.butterfaces.test.simple;

import de.larmic.butterfaces.test.Deployments;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * Simple Arquillian test using {@link org.jboss.shrinkwrap.api.spec.JavaArchive} and wildfly as container.
 */
@RunWith(Arquillian.class)
public class GretterTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return Deployments.createGreeterDeployment();
    }

    @Inject
    private Greeter greeter;

    /**
     * Tests injecting cdi bean and call method on it.
     */
    @Test
    public void should_create_greeting() {
        Assert.assertEquals("Hello, Earthling!", greeter.createGreeting("Earthling"));
        greeter.greet(System.out, "Earthling");
    }

}
