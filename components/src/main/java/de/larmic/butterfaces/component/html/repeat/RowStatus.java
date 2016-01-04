/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.repeat;

import de.larmic.butterfaces.util.StringJoiner;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Row iteration status provides by &lt;b:repeat&gt; for each row.
 *
 * @author Lars Michaelis
 */
public final class RowStatus implements Serializable {

    private final int index;
    private final Integer rowCount;
    private final boolean firstElement;
    private final boolean lastElement;
    private final boolean evenElement;

    public RowStatus(int actualRowIndex, int maxRowCount) {
        this.index = actualRowIndex;
        this.rowCount = maxRowCount;
        this.firstElement = (actualRowIndex == 0);
        this.lastElement = (actualRowIndex >= maxRowCount - 1);
        this.evenElement = (((actualRowIndex + 1) % 2) == 0);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return StringJoiner.on(", ").join(Arrays.asList(
                "index=" + index,
                "firstElement=" + firstElement,
                "lastElement=" + lastElement,
                "evenElement=" + evenElement,
                "rowCount=" + rowCount)).toString();
    }
}
