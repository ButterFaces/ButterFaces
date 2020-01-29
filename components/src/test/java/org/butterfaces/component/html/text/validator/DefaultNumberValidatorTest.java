package org.butterfaces.component.html.text.validator;

import org.butterfaces.component.html.text.HtmlNumber;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.faces.validator.ValidatorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultNumberValidatorTest {

    private final DefaultNumberValidator validator = new DefaultNumberValidator();

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void validateWithValueIsBlankAndMinAndMaxIsNull(final String value) {
        final HtmlNumber htmlNumber = createHtmlNumberMock(null, null);

        assertDoesNotThrow(() -> validator.validate(null, htmlNumber, value));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void validateWithValueIsBlankAndMinAndMaxIsEmpty(final String value) {
        final HtmlNumber htmlNumber = createHtmlNumberMock("", "");

        assertDoesNotThrow(() -> validator.validate(null, htmlNumber, value));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void validateWithValueIsBlankAndMinAndMaxIsSet(final String value) {
        final HtmlNumber htmlNumber = createHtmlNumberMock("0", "10");

        final ValidatorException exception = assertThrows(ValidatorException.class, () -> validator.validate(null, htmlNumber, value));

        assertThat(exception.getFacesMessage().getSummary()).isEqualTo("No number");
        assertThat(exception.getFacesMessage().getDetail()).isEqualTo(value + " is no number");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void validateWithValueIsBlankAndMinIsNotSetAndMaxIsSet(final String value) {
        final HtmlNumber htmlNumber = createHtmlNumberMock(null, "10");

        final ValidatorException exception = assertThrows(ValidatorException.class, () -> validator.validate(null, htmlNumber, value));

        assertThat(exception.getFacesMessage().getSummary()).isEqualTo("No number");
        assertThat(exception.getFacesMessage().getDetail()).isEqualTo(value + " is no number");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void validateWithValueIsBlankAndMinIsSetAndMaxIsNotSet(final String value) {
        final HtmlNumber htmlNumber = createHtmlNumberMock("0", null);

        final ValidatorException exception = assertThrows(ValidatorException.class, () -> validator.validate(null, htmlNumber, value));

        assertThat(exception.getFacesMessage().getSummary()).isEqualTo("No number");
        assertThat(exception.getFacesMessage().getDetail()).isEqualTo(value + " is no number");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3"})
    void validateWithValueInRange(final String value) {
        final HtmlNumber htmlNumber = createHtmlNumberMock("1", "3");

        assertDoesNotThrow(() -> validator.validate(null, htmlNumber, value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "4", "5"})
    void validateWithValueOutOfRange(final String value) {
        final HtmlNumber htmlNumber = createHtmlNumberMock("1", "3");

        final ValidatorException exception = assertThrows(ValidatorException.class, () -> validator.validate(null, htmlNumber, value));

        if (Integer.parseInt(value) < 1) {
            assertThat(exception.getFacesMessage().getSummary()).isEqualTo("Number is to small");
            assertThat(exception.getFacesMessage().getDetail()).isEqualTo(value + " is to small");
        } else {
            assertThat(exception.getFacesMessage().getSummary()).isEqualTo("Number is to big");
            assertThat(exception.getFacesMessage().getDetail()).isEqualTo(value + " is to big");
        }
    }

    private HtmlNumber createHtmlNumberMock(final String min, final String max) {
        final HtmlNumber htmlNumber = mock(HtmlNumber.class);
        when(htmlNumber.getMin()).thenReturn(min);
        when(htmlNumber.getMax()).thenReturn(max);
        return htmlNumber;
    }
}