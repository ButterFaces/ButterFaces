/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.resolver;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lars Michaelis
 */
public class MustacheResolverTest {

    @Test
    public void testGetMustacheKeysWithEmptyText() throws Exception {
        assertThat(MustacheResolver.getMustacheKeys(null)).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("")).isEmpty();
    }

    @Test
    public void testGetMustacheKeysWithNoMatch() throws Exception {
        assertThat(MustacheResolver.getMustacheKeys("test")).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("{test}")).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("{{test}")).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("{test}}")).isEmpty();
    }

    @Test
    public void testGetMustacheKeysWithSingleMatch() throws Exception {
        assertThat(MustacheResolver.getMustacheKeys("{{test}}")).containsExactly("test");
        assertThat(MustacheResolver.getMustacheKeys("{{test}}and{{test}}")).containsExactly("test");
    }

    @Test
    public void testGetMustacheKeysWithSingleMatchSurroundedByText() throws Exception {
        assertThat(MustacheResolver.getMustacheKeys("aa{{test}}bb")).containsExactly("test");
    }

    @Test
    public void testGetMustacheKeysWithMultipleMatch() throws Exception {
        assertThat(MustacheResolver.getMustacheKeys("{{foo}}and{{bar}}")).contains("foo", "bar");
    }

    @Test
    public void testGetMustacheKeysWithDotNotation() throws Exception {
        assertThat(MustacheResolver.getMustacheKeys("{{foo.bar}}")).containsExactly("foo.bar");
    }

}