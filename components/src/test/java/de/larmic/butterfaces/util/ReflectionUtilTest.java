/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lars Michaelis
 */
public class ReflectionUtilTest {

    @Test
    public void testGetValueFromObjectByProperty() throws Exception {
        assertThat(new ReflectionUtil().getValueFromObject(new DemoPojo(), "aProperty")).isEqualTo("aPropertyByField");
    }

    @Test
    public void testGetValueFromObjectByGetter() throws Exception {
        assertThat(new ReflectionUtil().getValueFromObject(new DemoPojo(), "bProperty")).isEqualTo("bPropertyByGetter");
    }

    @Test
    public void testGetValuePropertyByField() throws Exception {
        assertThat(new ReflectionUtil().getValuePropertyByField(new DemoPojo(), "aProperty")).isEqualTo("aPropertyByField");
        assertThat(new ReflectionUtil().getValuePropertyByField(new DemoPojo(), "bProperty")).isNull();
    }

    @Test
    public void testGetValuePropertyByGetter() throws Exception {
        assertThat(new ReflectionUtil().getValuePropertyByGetter(new DemoPojo(), "bProperty")).isEqualTo("bPropertyByGetter");
        assertThat(new ReflectionUtil().getValuePropertyByGetter(new DemoPojo(), "aProperty")).isNull();
    }

    public class DemoPojo {
        private final String aProperty = "aPropertyByField";

        public String getBProperty() {
            return "bPropertyByGetter";
        }
    }
}