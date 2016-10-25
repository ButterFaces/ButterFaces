package de.larmic.butterfaces.component.renderkit.html_basic.text.util;

import de.larmic.butterfaces.component.html.text.HtmlTags;
import de.larmic.butterfaces.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public class FreeTextSeparators {

    public static List<String> getFreeTextSeparators(HtmlTags tags) {
        final String confirmKeys = tags.getConfirmKeys();

        if (StringUtils.isNotEmpty(confirmKeys)) {
            return Arrays.asList(confirmKeys.split("(?)"));
        }

        return Arrays.asList(",", " ");

    }
}
