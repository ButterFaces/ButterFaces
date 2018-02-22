package org.butterfaces.component.showcase.tree;

import java.util.ArrayList;
import java.util.List;

public class Episodes {

   public static final List<Episode> EPISODES = new ArrayList<>();

   private static final String IMAGE_PATH = "resources/images/treebox/";

   static {
      EPISODES.add(createEpisode(1, "Children of the Gods 1/2", "Mario Azzopardi", "Jonathan Glassner & Brad Wright", "July 27, 1997", "ChildrenoftheGods","jpg"));
      EPISODES.add(createEpisode(2, "Children of the Gods 2/2", "Mario Azzopardi", "Jonathan Glassner & Brad Wright", "July 27, 1997", "ChildrenoftheGods","jpg"));
      EPISODES.add(createEpisode(3, "The Enemy Within", "Dennis Berry", "Brad Wright", "August 1, 1997", "TheEnemyWithin","jpg"));
      EPISODES.add(createEpisode(4, "Emancipation", "Jeff Woolnough", "Katharyn Michaelian Powers", "August 8, 1997", "Emancipation","jpg"));
      EPISODES.add(createEpisode(5, "The Broca Divide", "Bill Gereghty", "Jonathan Glassner", "August 15, 1997", "TheBrocaDivide","jpg"));
      EPISODES.add(createEpisode(6, "The First Commandment", "Dennis Berry", "Robert C. Cooper", "August 22, 1997", "TheFirstCommandment","jpg"));
      EPISODES.add(createEpisode(7, "Cold Lazarus", "Kenneth J. Girotti", "Jeff F. King", "August 29, 1997", "ColdLazarus","jpg"));
      EPISODES.add(createEpisode(8, "The Nox", "Brad Turner", "Hart Hanson", "September 12, 1997", "TheNox","jpg"));
      EPISODES.add(createEpisode(9, "Brief Candle", "Kenneth J. Girotti", "Jeff F. King", "September 19, 1997", "BriefCandle","jpg"));
      EPISODES.add(createEpisode(10, "Thor's Hammer", "Brad Turner", "Katharyn Michaelian Powers", "September 26, 1997", "ThorsHammer","jpg"));
      EPISODES.add(createEpisode(11, "The Torment of Tantalus", "Jonathan Glassner", "Robert C. Cooper", "October 3, 1997", "TheTormentofTantalus","jpg"));
      EPISODES.add(createEpisode(12, "Bloodlines", "Mario Azzopardi", "Jeff F. King", "October 10, 1997", "Bloodlines","jpg"));
      EPISODES.add(createEpisode(13, "Fire and Water", "Allan Eastman", "Katharyn Michaelian Powers", "October 17, 1997", "FireandWater","jpg"));
      EPISODES.add(createEpisode(14, "Hathor", "Brad Turner", "Story: David Bennett Carren & J. Larry Carroll", "October 24, 1997", "Hathor","jpg"));
      EPISODES.add(createEpisode(15, "Singularity", "Mario Azzopardi", "Robert C. Cooper", "October 31, 1997", "Singularity","jpg"));
      EPISODES.add(createEpisode(17, "Enigma", "Bill Gereghty", "Katharyn Michaelian Powers", "January 30, 1998", "Enigma","jpg"));
      EPISODES.add(createEpisode(18, "Solitudes", "Martin Wood", "Brad Wright", "February 6, 1998", "Solitudes","jpg"));
      EPISODES.add(createEpisode(19, "Tin Man", "Jimmy Kaufman", "Jeff F. King", "February 13, 1998", "TinMan","jpg"));
      EPISODES.add(createEpisode(20, "There But for the Grace of God", "David Warry-Smith", "Robert C. Cooper", "February 20, 1998", "ThereButfortheGraceofGod","jpg"));
      EPISODES.add(createEpisode(21, "Politics", "Martin Wood", "Brad Wright", "February 27, 1998", "Politics","jpg"));
      EPISODES.add(createEpisode(22, "Within the Serpent's Grasp", "David Warry-Smith", "Story: James Crocker", "March 6, 1998", "WithintheSerpentsGrasp","jpg"));
   }

   private static Episode createEpisode(int numberInSeries, String title, String directedBy, String writtenBy,
                                        String originalAirDate, String imageName, String imageExtension) {
      final Episode episode = new Episode();
      episode.setNumberInSeries(numberInSeries);
      episode.setTitle(title);
      episode.setDirectedBy(directedBy);
      episode.setWrittenBy(writtenBy);
      episode.setOriginalAirDate(originalAirDate);
      episode.setImage(new Image(IMAGE_PATH, imageName, imageExtension));
      return episode;
   }
}
