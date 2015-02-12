package de.larmic.butterfaces.test;

import de.larmic.butterfaces.test.simple.Greeter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by larmic on 04.09.14.
 */
public class Deployments {

    public static final String SHOWCASE_PATH = "/showcase/target/showcase.war";

    public static JavaArchive createGreeterDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Greeter.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static WebArchive createShowcaseDeployment() {
        final URI absoluteTestExecutionPath = new File("").toURI();
        try {
            // default showcase path when calling unit test by IDE
            File showcase = new File(new URI(absoluteTestExecutionPath + SHOWCASE_PATH));

            if (!showcase.exists()) {
                // unit test is calling by maven so use correct file path
                showcase = new File(new URI(absoluteTestExecutionPath + "../" + SHOWCASE_PATH));

                if (!showcase.exists()) {
                    Assert.fail("Could not find showcase.war in " + showcase.getAbsolutePath());
                    return null;
                }
            }

            return ShrinkWrap.create(ZipImporter.class, "showcase.war").importFrom(showcase).as(WebArchive.class);
        } catch (URISyntaxException e) {
            Assert.fail("Could not find showcase.war");
            return null;
        }
    }

}
