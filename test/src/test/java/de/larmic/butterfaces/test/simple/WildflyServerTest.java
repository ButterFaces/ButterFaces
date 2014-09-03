package de.larmic.butterfaces.test.simple;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Simple test to start wildfly server by using arquillian.
 */
@RunWith(Arquillian.class)
public class WildflyServerTest {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(ZipImporter.class, "showcase.war")
                .importFrom(new File("/Users/larmic/Work/Private/workspace/butterfaces/showcase/target/showcase-1.5.6-SNAPSHOT.war"))
                .as(WebArchive.class);
    }

    /**
     * Run test endless for manually test.
     */
    @Test
    @Ignore
    public void runWildflyEndless() {
        while (true) {

        }
    }

}
