/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.showcase.table;

import java.util.Date;

/**
 * @author Lars Michaelis
 */
public class DemoPojo {

    private final long id;
    private final Date date;
    private String a;
    private String b;

    public DemoPojo(final long id, final String a, final String b) {
        this.id = id;
        this.date = new Date(new Date().getTime() + (id * 1000));
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

    public Date getDate() {
        return date;
    }

    public Date getActualDate() {
        return new Date();
    }
}
