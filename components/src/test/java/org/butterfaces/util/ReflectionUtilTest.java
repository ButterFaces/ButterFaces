/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lars Michaelis
 */
public class ReflectionUtilTest {

    @Test
    public void testGetStringValueFromObjectByProperty() throws Exception {
        assertThat(new ReflectionUtil().getStringValueFromObject(new DemoPojo(), "aProperty")).isEqualTo("aPropertyByField");
    }

    @Test
    public void testGetStringValueFromObjectByGetter() throws Exception {
        assertThat(new ReflectionUtil().getStringValueFromObject(new DemoPojo(), "bProperty")).isEqualTo("bPropertyByGetter");
    }

    @Test
    public void testGetValueFromObjectByGetter() throws Exception {
        assertThat(new ReflectionUtil().getValueFromObject(new DemoPojo(), "innerDemoPojo", InnerDemoPojo.class)).hasSameClassAs(new InnerDemoPojo()).isNotNull();
    }

    @Test
    public void testGetValuePropertyWithDotNotation() throws Exception {
        assertThat(new ReflectionUtil().getValueFromObject(new DemoPojo(), "innerDemoPojo.cProperty", String.class)).isEqualTo("cPropertyByField");
        assertThat(new ReflectionUtil().getValueFromObject(new DemoPojo(), "innerDemoPojo.dProperty", String.class)).isEqualTo("dPropertyByGetter");
        assertThat(new ReflectionUtil().getValueFromObject(new DemoPojo(), "innerDemoPojo.innerInnerDemoPojo.eProperty", String.class)).isEqualTo("ePropertyByField");
        assertThat(new ReflectionUtil().getValueFromObject(new DemoPojo(), "innerDemoPojo.innerInnerDemoPojo.fProperty", String.class)).isEqualTo("fPropertyByGetter");
    }

    @Test
    public void testGetStringValuePropertyWithDotNotation() throws Exception {
        assertThat(new ReflectionUtil().getStringValueFromObject(new DemoPojo(), "innerDemoPojo.cProperty")).isEqualTo("cPropertyByField");
        assertThat(new ReflectionUtil().getStringValueFromObject(new DemoPojo(), "innerDemoPojo.dProperty")).isEqualTo("dPropertyByGetter");
        assertThat(new ReflectionUtil().getStringValueFromObject(new DemoPojo(), "innerDemoPojo.innerInnerDemoPojo.eProperty")).isEqualTo("ePropertyByField");
        assertThat(new ReflectionUtil().getStringValueFromObject(new DemoPojo(), "innerDemoPojo.innerInnerDemoPojo.fProperty")).isEqualTo("fPropertyByGetter");
    }

    public class DemoPojo {
        private final String aProperty = "aPropertyByField";

        public String getBProperty() {
            return "bPropertyByGetter";
        }

        public InnerDemoPojo getInnerDemoPojo() {
            return new InnerDemoPojo();
        }
    }

    public class InnerDemoPojo {
        private final String cProperty = "cPropertyByField";
        private final InnerInnerDemoPojo innerInnerDemoPojo = new InnerInnerDemoPojo();

        public String getDProperty() {
            return "dPropertyByGetter";
        }
    }

    public class InnerInnerDemoPojo {
        private final String eProperty = "ePropertyByField";

        public String getFProperty() {
            return "fPropertyByGetter";
        }
    }
}