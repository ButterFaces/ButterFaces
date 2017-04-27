package de.larmic.butterfaces.component.partrenderer;

import org.junit.Test;

import static de.larmic.butterfaces.component.partrenderer.RenderUtils.createJQueryBySelector;
import static org.assertj.core.api.Assertions.assertThat;

public class RenderUtilsTest {

    @Test
    public void testCreateJQueryBySelector() throws Exception {
        assertThat(createJQueryBySelector("test-element-id-1", null)).isEqualTo("jQuery(document.getElementById('test-element-id-1'))");
        assertThat(createJQueryBySelector("test-element-id-2", "")).isEqualTo("jQuery(document.getElementById('test-element-id-2'))");
        assertThat(createJQueryBySelector("test-element-id-3", "test-child-id")).isEqualTo("jQuery(document.getElementById('test-element-id-3')).find('test-child-id')");
    }

}