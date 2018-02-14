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
        return createDependency("Bootstrap 4.0, JQuery 3.3.1","org.butterfaces", "components", version.getLastestReleaseVersion());
    }

    public String getButterFacesV2MavenDependency() {
        return createDependency("Bootstrap 3.3.7, JQuery 2.7.1","de.larmic.butterfaces", "components", "2.1.25");
    }

    private String createDependency(String hint, String groupId, String artifactId, String version) {
        final StringBuilder sb = new StringBuilder();

        sb.append("<dependency>\n");
        sb.append("   <!-- " + hint + "-->\n");
        sb.append("   <groupId>" + groupId + "</groupId>\n");
        sb.append("   <artifactId>" + artifactId + "</artifactId>\n");
        sb.append("   <version>" + version + "</version>\n");
        sb.append("</dependency>");

        return sb.toString();
    }

}
