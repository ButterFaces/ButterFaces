package org.butterfaces.component.html.text.validator;

import org.butterfaces.component.html.text.HtmlNumber;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultNumberValidatorTest {

    private final DefaultNumberValidator validator = new DefaultNumberValidator();

    @Test
    @Disabled
    void validateWithValueIsNull() {
        final HtmlNumber htmlNumber = createHtmlNumberMock("0", "10");

        validator.validate(null, htmlNumber, null);

        assertThat(true).isFalse();
    }

    private HtmlNumber createHtmlNumberMock(final String min, final String max) {
        final HtmlNumber htmlNumber = mock(HtmlNumber.class);
        when(htmlNumber.getMin()).thenReturn(min);
        when(htmlNumber.getMax()).thenReturn(max);
        return htmlNumber;
    }
}