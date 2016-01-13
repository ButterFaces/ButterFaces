package de.larmic.butterfaces.component.showcase.comboBox;

public class Episode {
   private int numberInSeries;
   private String title;
   private String directedBy;
   private String writtenBy;
   private String originalAirDate;
   private String image;

   public int getNumberInSeries() {
      return numberInSeries;
   }

   public void setNumberInSeries(int numberInSeries) {
      this.numberInSeries = numberInSeries;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDirectedBy() {
      return directedBy;
   }

   public void setDirectedBy(String directedBy) {
      this.directedBy = directedBy;
   }

   public String getWrittenBy() {
      return writtenBy;
   }

   public void setWrittenBy(String writtenBy) {
      this.writtenBy = writtenBy;
   }

   public String getOriginalAirDate() {
      return originalAirDate;
   }

   public void setOriginalAirDate(String originalAirDate) {
      this.originalAirDate = originalAirDate;
   }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }

    @Override
    public String toString() {
        return title;
    }
}
