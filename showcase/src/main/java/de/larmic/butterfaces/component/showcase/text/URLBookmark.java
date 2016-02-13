package de.larmic.butterfaces.component.showcase.text;

/**
 * Created by larmic on 16.09.15.
 */
public class URLBookmark {

    private String fullURL;

    public URLBookmark(String fullURL) {
        this.fullURL = fullURL;
    }

    public String getFullURL() {
        return fullURL;
    }

    public void setFullURL(String fullURL) {
        this.fullURL = fullURL;
    }

    public String toString(){
        return fullURL;
    }

}
