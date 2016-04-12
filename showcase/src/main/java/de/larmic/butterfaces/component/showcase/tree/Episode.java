/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.showcase.tree;

/**
 * @author Stephan Zerhusen
 */
public class Episode {
    private Image image;
    private int numberInSeries;
    private String title;
    private String directedBy;
    private String writtenBy;
    private String originalAirDate;

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getImageUrl() {
        return image.getUrl();
    }

    @Override
    public String toString() {
        return title;
    }
}
