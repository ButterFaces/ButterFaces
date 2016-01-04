/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.repeat;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lars Michaelis
 */
public class RowStatusTest {

    @Test
    public void testToString() throws Exception {
        assertThat(new RowStatus(0, 10).toString()).isEqualTo("index=0, firstElement=true, lastElement=false, evenElement=false, rowCount=10");
        assertThat(new RowStatus(5, 10).toString()).isEqualTo("index=5, firstElement=false, lastElement=false, evenElement=true, rowCount=10");
        assertThat(new RowStatus(9, 10).toString()).isEqualTo("index=9, firstElement=false, lastElement=true, evenElement=true, rowCount=10");
    }

}