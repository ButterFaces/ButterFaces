package de.larmic.butterfaces.component.showcase;

import java.io.Serializable;

/**
 * Contains butterfaces maven pom.xml informations. templating-maven-plugin is used in pom.xml.
 */
@javax.inject.Named
@javax.faces.view.ViewScoped
@SuppressWarnings("serial")
public class Version implements Serializable {

    private static final String ERROR_VERSION = "1.9.0.CR5";

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
            final String version = VERSION.replaceAll("-SNAPSHOT", "");
            final String[] splitted = version.split("\\.");
            final Integer minorVersion = getMinorVersion(splitted[splitted.length - 1]);

            if (minorVersion != null) {
                final StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < splitted.length - 1; i++) {
                    stringBuilder.append(splitted[i]);
                    stringBuilder.append(".");
                }
                stringBuilder.append(minorVersion);
            }

            return ERROR_VERSION;
        }

        return VERSION;
    }

    /**
     * @return null if value is not parsable or value is less than 0.
     */
    private Integer getMinorVersion(final String value) {
        try {
            final Integer newMinorVersion = Integer.valueOf(value) - 1;
            return newMinorVersion >= 0 ? newMinorVersion : null;
        } catch (NumberFormatException e) {
            return null;
        }
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