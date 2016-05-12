package de.larmic.butterfaces.component.showcase;

import java.io.Serializable;
import java.lang.String;
import java.lang.StringBuilder;

/**
 * Contains butterfaces maven pom.xml informations. templating-maven-plugin is used in pom.xml.
 */
@javax.inject.Named
@javax.faces.view.ViewScoped
@SuppressWarnings("serial")
public class Version implements Serializable {

    private static final String VERSION = "${project.version}";
    private static final String GROUPID = "${project.groupId}";
    private static final String ARTIFACTID = "${project.artifactId}";
    private static final String REVISION = "${buildNumber}";
    private static final String JBOSS = "${version.jbossas}";

    public String getVersion() {
        return VERSION;
    }

    public String getLastestReleaseVersion() {
        if (VERSION.endsWith("SNAPSHOT")) {
        }

        return VERSION;
    }

    public String getGroupId() {
        return GROUPID;
    }

    public String getArtifactId() {
        return ARTIFACTID;
    }

    public String getRevision() {
        return REVISION;
    }

    public String getJBossVersion() {
        return JBOSS;
    }
}