/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.model.tree;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Lars Michaelis
 */
public class EnumTreeBoxWrapperTest {

    @Test
    public void testEquals() throws Exception {
        final EnumTreeBoxWrapper enumTreeBoxWrapper1 = new EnumTreeBoxWrapper(TestEnum.TEST1, "test1");
        final EnumTreeBoxWrapper enumTreeBoxWrapper2 = new EnumTreeBoxWrapper(TestEnum.TEST2, "test2");

        assertThat(enumTreeBoxWrapper1).isEqualTo(enumTreeBoxWrapper1);
        assertThat(enumTreeBoxWrapper1).isEqualTo(new EnumTreeBoxWrapper(TestEnum.TEST1, "test1"));
        assertThat(enumTreeBoxWrapper1).isEqualTo(TestEnum.TEST1);
        assertThat(enumTreeBoxWrapper1).isNotEqualTo(enumTreeBoxWrapper2);
        assertThat(enumTreeBoxWrapper1).isNotEqualTo(TestEnum.TEST2);
    }

    @Test
    public void testToString() throws Exception {
        assertThat(new EnumTreeBoxWrapper(TestEnum.TEST1, "test1").toString()).isEqualTo("test1");
        assertThat(new EnumTreeBoxWrapper(TestEnum.TEST2, "test2").toString()).isEqualTo("test2");
    }
}