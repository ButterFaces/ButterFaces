package de.larmic.butterfaces.component.showcase.table;

/**
 * Created by larmic on 21.11.14.
 */
public class StringPair {

    private final long id;
    private String a;
    private String b;

    public StringPair(final long id, final String a, final String b) {
        this.id = id;
        this.a = a;
        this.b = b;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public long getId() {
        return id;
    }

    public long getIdentifier() {
        return id;
    }
}
