package de.larmic.butterfaces.component.renderkit.html_basic.text.util;

import de.larmic.butterfaces.component.html.text.HtmlTags;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FreeTextSeparatorsTest {

    @Test
    public void getFreeTextSeparators() throws Exception {
        final HtmlTags tags = mock(HtmlTags.class);
        when(tags.getConfirmKeys())
                .thenReturn(",")
                .thenReturn(", ")
                .thenReturn(null)
                .thenReturn("")
                .thenReturn("; ,");

        assertThat(FreeTextSeparators.getFreeTextSeparators(tags)).containsExactly(",");
        assertThat(FreeTextSeparators.getFreeTextSeparators(tags)).containsExactly(",", " ");
        assertThat(FreeTextSeparators.getFreeTextSeparators(tags)).containsExactly(",", " ");
        assertThat(FreeTextSeparators.getFreeTextSeparators(tags)).containsExactly(",", " ");
        assertThat(FreeTextSeparators.getFreeTextSeparators(tags)).containsExactly(";", " ", ",");
    }
}