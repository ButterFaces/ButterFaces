/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.resolver;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lars Michaelis
 */
class MustacheResolverTest {

    @Test
    void testGetMustacheKeysWithEmptyText() {
        assertThat(MustacheResolver.getMustacheKeys(null)).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("")).isEmpty();
    }

    @Test
    void testGetMustacheKeysWithNoMatch() {
        assertThat(MustacheResolver.getMustacheKeys("test")).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("{test}")).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("{{test}")).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("{test}}")).isEmpty();
    }

    @Test
    void testGetMustacheKeysWithSingleMatch() {
        assertThat(MustacheResolver.getMustacheKeys("{{test}}")).containsExactly("test");
        assertThat(MustacheResolver.getMustacheKeys("{{test}}and{{test}}")).containsExactly("test");
    }

    @Test
    void testGetMustacheKeysWithSingleMatchSurroundedByText() {
        assertThat(MustacheResolver.getMustacheKeys("aa{{test}}bb")).containsExactly("test");
    }

    @Test
    void testGetMustacheKeysWithMultipleMatch() {
        assertThat(MustacheResolver.getMustacheKeys("{{foo}}and{{bar}}")).contains("foo", "bar");
    }

    @Test
    void testGetMustacheKeysWithDotNotation() {
        assertThat(MustacheResolver.getMustacheKeys("{{foo.bar}}")).containsExactly("foo.bar");
    }

}