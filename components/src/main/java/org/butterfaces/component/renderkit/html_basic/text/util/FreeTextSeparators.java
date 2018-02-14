package org.butterfaces.component.renderkit.html_basic.text.util;

import org.butterfaces.component.html.text.HtmlTags;
import org.butterfaces.util.StringUtils;
import org.butterfaces.util.StringUtils;

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
