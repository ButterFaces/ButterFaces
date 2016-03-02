/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Lars Michaelis
 */
public class ReflectionUtil {

    public String getStringValueFromObject(final Object object, final String fieldName) {
        Object value = getValuePropertyByField(object, fieldName, Object.class);

        if (value == null) {
            value = getValuePropertyByGetter(object, fieldName, Object.class);
        }

        return convertToString(value);
    }

    public <T> T getValueFromObject(final Object object, final String fieldName, final Class<T> valueClass) {
        return (T) getPlainValueFromObject(object, fieldName);
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
