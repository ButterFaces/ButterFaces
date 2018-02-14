package org.butterfaces.component.showcase;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class DependencyExample implements Serializable {

    @Inject
    private org.butterfaces.component.showcase.Version version;

    public String getButterFacesMavenDependency() {
        return createDependency("components");    }

    private String createDependency(String artifactId) {
        final StringBuilder sb = new StringBuilder();

        sb.append("<dependency>\n");
        sb.append("   <groupId>org.butterfaces</groupId>\n");
        sb.append("   <artifactId>" + artifactId + "</artifactId>\n");
        sb.append("   <version>" + version.getLastestReleaseVersion() + "</version>\n");
        sb.append("</dependency>");

        return sb.toString();
    }

}
