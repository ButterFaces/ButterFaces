package de.larmic.butterfaces.test;

import de.larmic.butterfaces.test.simple.Greeter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;

import java.io.File;

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
        final String absoluteTestExecutionPath = new File("./").getAbsolutePath();
        File showcase = new File(absoluteTestExecutionPath + SHOWCASE_PATH);

        if (!showcase.exists()) {
            System.out.println("ZZZZ:" + absoluteTestExecutionPath);

            if (absoluteTestExecutionPath.endsWith("test/.")) {
                // Pathes are not equal wenn starting test in IDE or by maven goal.
                System.out.println("ZZZZ:" + absoluteTestExecutionPath + "./" + SHOWCASE_PATH);
                showcase = new File(absoluteTestExecutionPath + "./" + SHOWCASE_PATH);
            } else {
                Assert.fail("Could not find showcase.war");
                return null;
            }
        }

        return ShrinkWrap.create(ZipImporter.class, "showcase.war").importFrom(showcase).as(WebArchive.class);
    }

}
