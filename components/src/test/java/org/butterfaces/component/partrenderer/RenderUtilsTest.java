package org.butterfaces.component.partrenderer;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class RenderUtilsTest {

    @Test
    public void testCreateJQueryBySelector() throws Exception {
        Assertions.assertThat(RenderUtils.createJQueryBySelector("test-element-id-1", null)).isEqualTo("jQuery(document.getElementById('test-element-id-1'))");
        Assertions.assertThat(RenderUtils.createJQueryBySelector("test-element-id-2", "")).isEqualTo("jQuery(document.getElementById('test-element-id-2'))");
        Assertions.assertThat(RenderUtils.createJQueryBySelector("test-element-id-3", "test-child-id")).isEqualTo("jQuery(document.getElementById('test-element-id-3')).find('test-child-id')");
    }

}