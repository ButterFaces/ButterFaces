/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lars Michaelis
 */
public class StringJoinerTest {

    public static final List<String> VALUES = Arrays.asList("ding", "dong", "test", "me");

    @Test
    public void testJoinWithCommaSeparator() throws Exception {
        assertThat(StringJoiner.on(',').join(VALUES).toString()).isEqualTo("ding,dong,test,me");
        assertThat(StringJoiner.on(", ").join(VALUES).toString()).isEqualTo("ding, dong, test, me");
    }

    @Test
    public void testJoinWithSpaceSeparator() throws Exception {
        assertThat(StringJoiner.on(" ").join(VALUES).toString()).isEqualTo("ding dong test me");
    }

    @Test
    public void testJoinWithWrapper() throws Exception {
        assertThat(StringJoiner.on('|').join(VALUES).wrappedBy("'").toString()).isEqualTo("'ding'|'dong'|'test'|'me'");
        assertThat(StringJoiner.on('|').join(VALUES).wrappedBy('\'').toString()).isEqualTo("'ding'|'dong'|'test'|'me'");
        assertThat(StringJoiner.on(" | ").join(VALUES).wrappedBy("'").toString()).isEqualTo("'ding' | 'dong' | 'test' | 'me'");
    }

    @Test
    public void testChainingJoins() throws Exception {
        assertThat(StringJoiner.on(" ").join("ding").join("dong").toString()).isEqualTo("ding dong");
        assertThat(StringJoiner.on(" ").join(VALUES).join("too").toString()).isEqualTo("ding dong test me too");
        assertThat(StringJoiner.on(" ").join("hi").join(VALUES).toString()).isEqualTo("hi ding dong test me");
    }
}