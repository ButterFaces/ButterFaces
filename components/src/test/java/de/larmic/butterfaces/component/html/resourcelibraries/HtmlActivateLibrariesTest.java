package de.larmic.butterfaces.component.html.resourcelibraries;

import org.junit.Assert;
import org.junit.Test;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlActivateLibrariesTest {

    @Test
    public void testAssertThatAllResourcesThatAreAnnotatedAreFoundInResourcesFolders() throws Exception {
        final List<ResourceDependency> resourceDependencies = this.loadResourceDependencies();

        for (ResourceDependency resourceDependency : resourceDependencies) {
            if ("butterfaces-dist-js".equals(resourceDependency.library())) {
                // will be checked in other test
                continue;
            }

            final List<String> fileNames = this.loadResourcesFileNamesFromResourceSubDirectory(resourceDependency.library());

            boolean foundResourceByName = false;

            for (String fileName : fileNames) {
                if (fileName.equals(resourceDependency.name())) {
                    foundResourceByName = true;
                    break;
                }
            }

            Assert.assertTrue("Resource '" + resourceDependency.name() + "' not found", foundResourceByName);
        }
    }

    @Test
    public void testAssertThatAllResourcesThatAreAnnotatedAreFoundInResourcesFoldersCreatedByLess() throws Exception {
        final List<ResourceDependency> resourceDependencies = this.loadResourceDependencies();

        for (ResourceDependency resourceDependency : resourceDependencies) {
            if (!"butterfaces-dist".equals(resourceDependency.library())) {
                // will be checked in other test
                continue;
            }

            final List<String> fileNames = this.loadResourcesFileNamesFromResourceSubDirectory(resourceDependency.library() + "/css");

            boolean foundResourceByName = false;

            for (String fileName : fileNames) {
                if (("css/" + fileName).equals(resourceDependency.name())) {
                    foundResourceByName = true;
                    break;
                }
            }

            Assert.assertTrue("Resource '" + resourceDependency.name() + "' not found", foundResourceByName);
        }
    }

    @Test
    public void testAssertThatAllResourcesAreAnnotatedInResourceComponent() throws Exception {
        final List<ResourceDependency> resourceDependencies = this.loadResourceDependencies();

        this.assertResourcesInSubDirectory(resourceDependencies, "butterfaces-external");
        this.assertResourcesInSubDirectory(resourceDependencies, "butterfaces-js");
    }

    private void assertResourcesInSubDirectory(List<ResourceDependency> resourceDependencies, String subFolder) {
        for (String fileName : this.loadResourcesFileNamesFromResourceSubDirectory(subFolder)) {
            boolean foundResource = false;

            for (ResourceDependency resourceDependency : resourceDependencies) {
                if (fileName.equals(resourceDependency.name()) && subFolder.equals(resourceDependency.library())) {
                    foundResource = true;
                    break;
                }
            }

            Assert.assertTrue("Resource '" + fileName + "' not found", foundResource);
        }
    }

    private List<String> loadResourcesFileNamesFromResourceSubDirectory(final String subFolder) {
        final List<String> fileNames = new ArrayList<>();
        final URL resource = getClass().getResource("/META-INF/resources/" + subFolder + "/");

        for (File resourceFile : new File(resource.getFile()).listFiles()) {
            fileNames.add(resourceFile.getName());
        }

        return fileNames;
    }

    private List<ResourceDependency> loadResourceDependencies() {
        final Annotation[] annotations = HtmlActivateLibraries.class.getAnnotations();

        final List<ResourceDependency> resourceDependencies = new ArrayList<>();

        for (Annotation annotation : annotations) {
            if (annotation instanceof ResourceDependencies) {
                final ResourceDependencies resourceDependenciesAnnotation = (ResourceDependencies) annotation;
                resourceDependencies.addAll(Arrays.asList(resourceDependenciesAnnotation.value()));
            }
        }

        return resourceDependencies;
    }
}
