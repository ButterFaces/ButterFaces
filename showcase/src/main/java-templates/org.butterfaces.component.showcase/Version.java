package org.butterfaces.component.showcase;

import java.io.Serializable;

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
    private static final String JBOSS = "${version.wildfly.swarm}";

    public String getVersion() {
        return VERSION;
    }

    public String getLastestReleaseVersion() {
        if (VERSION.endsWith("SNAPSHOT")) {
            final String version = VERSION.replaceAll("-SNAPSHOT", "");
            try {
                final String[] splitted = version.split("\\.");
                final String newMinorVersion = Integer.valueOf(splitted[splitted.length - 1]) - 1 + "";

                final StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < splitted.length - 1; i++) {
                    stringBuilder.append(splitted[i]);
                    stringBuilder.append(".");
                }
                stringBuilder.append(newMinorVersion);

                return stringBuilder.toString();
            } catch (NumberFormatException e) {
                return version;
            }
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