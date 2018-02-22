/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Lars Michaelis
 */
public class ReflectionUtil {

    public static final char ATTRIBUTE_SEPARATOR = '.';

    public String getStringValueFromObject(final Object object, final String fieldName) {
        return convertToString(getValueFromObject(object, fieldName, Object.class));
    }

    public <T> T getValueFromObject(final Object object, final String fieldName, final Class<T> valueClass) {
        final int indexOfFirstDot = fieldName.indexOf(getAttributeSeparator());

        if (indexOfFirstDot > 0) {
            final String firstFieldName = fieldName.substring(0, indexOfFirstDot);
            final String restOfFieldName = fieldName.substring(indexOfFirstDot + 1);
            final Object innerObject = getPlainValueFromObject(object, firstFieldName);

            return getValueFromObject(innerObject, restOfFieldName, valueClass);
        }

        return (T) getPlainValueFromObject(object, fieldName);
    }

    /**
     * TODO Trivial components does not support {{foo.bar}} so ButterFaces replaces it by {{foo#bar}}.
     * TODO this could removed if https://github.com/trivial-components/trivial-components/issues/36 is fixed
     */
    protected char getAttributeSeparator() {
        return ATTRIBUTE_SEPARATOR;
    }

    private Object getPlainValueFromObject(final Object object, final String fieldName) {
        Object value = getValuePropertyByField(object, fieldName, Object.class);

        if (value == null) {
            value = getValuePropertyByGetter(object, fieldName, Object.class);
        }

        return value;
    }

    private <T> T getValuePropertyByField(final Object object, final String fieldName, final Class<T> valueClass) {
        try {
            final Field declaredField = object.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return (T) declaredField.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
        return null;
    }

    private <T> T getValuePropertyByGetter(final Object object, final String fieldName, final Class<T> valueClass) {
        try {
            final Method method = object.getClass().getMethod("get" + toUpperCase(fieldName));
            final Object valueObject = method.invoke(object, (Object[]) null);
            return (T) valueObject;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        }
        return null;
    }

    private String toUpperCase(final String str) {
        return Character.toString(str.charAt(0)).toUpperCase()+str.substring(1);
    }

    private String convertToString(final Object object) {
        if (object != null) {
            final String rowIdentifierAsString = object.toString();

            if (StringUtils.isNotEmpty(rowIdentifierAsString)) {
                return rowIdentifierAsString;
            }
        }

        return null;
    }

}
