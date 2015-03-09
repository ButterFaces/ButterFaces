package de.larmic.butterfaces.component.showcase;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by larmic on 09.03.15.
 */
@Named
@ViewScoped
public class DependencyExample implements Serializable {

    @Inject
    private de.larmic.butterfaces.component.showcase.Version version;

    public String getButterFacesMavenDependency() {
        final StringBuilder sb = new StringBuilder();

        sb.append("<dependency>\n");
        sb.append(" <groupId>de.larmic.butterfaces</groupId>\n");
        sb.append(" <artifactId>components</artifactId>\n");
        sb.append(" <version>" + version.getLastestReleaseVersion() + "</version>\n");
        sb.append("</dependency>");

        return sb.toString();
    }

}
