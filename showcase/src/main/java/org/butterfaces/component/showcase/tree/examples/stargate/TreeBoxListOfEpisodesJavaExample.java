/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.tree.examples.stargate;

import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;

/**
 * @author Lars Michaelis
 */
public class TreeBoxListOfEpisodesJavaExample extends JavaCodeExample {

    public TreeBoxListOfEpisodesJavaExample(String subPackage) {
        super("MyBean.java", "mybean", subPackage, "MyBean", true);

        this.addImport("import javax.faces.view.ViewScoped");
        this.addImport("import javax.inject.Named");

        this.appendInnerContent("    private Episode selectedValue;\n");
        this.appendInnerContent("    private List<Episode> episodes = new ArrayList<Episode>();\n");

        this.appendInnerContent("    public List<Episode> getValues() {");
        this.appendInnerContent("       if(episodes.isEmpty()) {");
        this.appendInnerContent("          episodes.add(createEpisode(1, \"Children of the Gods 1/2\",");
        this.appendInnerContent("                                     \"Mario Azzopardi\",");
        this.appendInnerContent("                                     \"Jonathan Glassner & Brad Wright\",");
        this.appendInnerContent("                                     \"July 27, 1997\",");
        this.appendInnerContent("                                     \"ChildrenoftheGods\", \"jpg\"));");
        this.appendInnerContent("          episodes.add(createEpisode(2, \"Children of the Gods 2/2\",");
        this.appendInnerContent("                                     \"Mario Azzopardi\",");
        this.appendInnerContent("                                     \"Jonathan Glassner & Brad Wright\",");
        this.appendInnerContent("                                     \"July 27, 1997\",");
        this.appendInnerContent("                                     \"ChildrenoftheGods\", \"jpg\"));");
        this.appendInnerContent("          ...");
        this.appendInnerContent("       }\n");
        this.appendInnerContent("        return episodes;");
        this.appendInnerContent("    }\n");

        this.appendInnerContent("    // GETTER + SETTER (selectedValue)\n");

        this.appendInnerContent("    private static Episode createEpisode(int numberInSeries,");
        this.appendInnerContent("                                         String title,");
        this.appendInnerContent("                                         String directedBy,");
        this.appendInnerContent("                                         String writtenBy,");
        this.appendInnerContent("                                         String originalAirDate,");
        this.appendInnerContent("                                         String imageName,");
        this.appendInnerContent("                                         String imageExtension) {");
        this.appendInnerContent("       final Episode episode = new Episode();");
        this.appendInnerContent("       episode.setNumberInSeries(numberInSeries);");
        this.appendInnerContent("       episode.setTitle(title);");
        this.appendInnerContent("       episode.setDirectedBy(directedBy);");
        this.appendInnerContent("       episode.setWrittenBy(writtenBy);");
        this.appendInnerContent("       episode.setOriginalAirDate(originalAirDate);");
        this.appendInnerContent("       episode.setImage(new Image(imageName, imageExtension));");
        this.appendInnerContent("       return episode;");
        this.appendInnerContent("    }");

    }
}
