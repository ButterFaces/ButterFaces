/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.util;

/**
 * TODO Trivial components does not support {{foo.bar}} so ButterFaces replaces it by {{foo#bar}}.
 * TODO this could removed if https://github.com/trivial-components/trivial-components/issues/36 is fixed
 * @author Lars Michaelis
 */
public class TrivialComponentsReflectionUtil extends ReflectionUtil {

    public static final char ATTRIBUTE_SEPARATOR = '#';

    @Override
    protected char getAttributeSeparator() {
        return ATTRIBUTE_SEPARATOR;
    }
}
