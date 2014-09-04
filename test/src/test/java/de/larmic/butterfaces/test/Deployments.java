package de.larmic.butterfaces.test;

import de.larmic.butterfaces.test.simple.Greeter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.io.File;

/**
 * Created by larmic on 04.09.14.
 */
public class Deployments {

    public static JavaArchive createGreeterDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(Greeter.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    public static WebArchive createShowcaseDeployment() {
        return ShrinkWrap.create(ZipImporter.class, "showcase.war")
                .importFrom(new File("/Users/larmic/Work/Private/workspace/butterfaces/showcase/target/showcase-1.5.6-SNAPSHOT.war"))
                .as(WebArchive.class);
    }

}
