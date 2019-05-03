/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lars Michaelis
 */
class StringUtilsTest {

    @Test
    void testIsEmpty() {
        assertThat(StringUtils.isEmpty(null)).isTrue();
        assertThat(StringUtils.isEmpty("")).isTrue();
        assertThat(StringUtils.isEmpty(" ")).isFalse();
        assertThat(StringUtils.isEmpty("foo")).isFalse();
    }

    @Test
    void testIsNotEmpty() {
        assertThat(StringUtils.isNotEmpty(null)).isFalse();
        assertThat(StringUtils.isNotEmpty("")).isFalse();
        assertThat(StringUtils.isNotEmpty(" ")).isTrue();
        assertThat(StringUtils.isNotEmpty("foo")).isTrue();
    }

}