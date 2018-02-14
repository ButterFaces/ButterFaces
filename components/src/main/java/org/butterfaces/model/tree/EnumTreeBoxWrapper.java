/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.model.tree;

/**
 * @author Lars Michaelis
 */
public class EnumTreeBoxWrapper {

    private final Enum enumValue;
    private final String translation;

    public EnumTreeBoxWrapper(Enum enumValue, String translation) {
        assert enumValue != null;
        this.enumValue = enumValue;
        this.translation = translation;
    }

    public Enum getEnumValue() {
        return enumValue;
    }

    public String getTranslation() {
        return translation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !(getClass() == o.getClass() || enumValue.getClass() == o.getClass())) {
            return false;
        }

        return o instanceof EnumTreeBoxWrapper ? enumValue.equals(((EnumTreeBoxWrapper) o).enumValue) : enumValue.equals(o);

    }

    @Override
    public int hashCode() {
        return enumValue.hashCode();
    }

    @Override
    public String toString() {
        return translation;
    }
}
