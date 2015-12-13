/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2015.
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

    public String getValueFromObject(final Object object, final String fieldName) {
        String value = getValuePropertyByField(object, fieldName);

        if (value == null) {
            value = getValuePropertyByGetter(object, fieldName);
        }

        return value;
    }

    public String getValuePropertyByField(final Object object, final String fieldName) {
        try {
            final Field declaredField = object.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);
            return convertToString(declaredField.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
        return null;
    }

    public String getValuePropertyByGetter(final Object object, final String fieldName) {
        try {
            final Method method = object.getClass().getMethod("get" + toUpperCase(fieldName));
            final Object valueObject = method.invoke(object, (Object[]) null);
            return convertToString(valueObject);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        }
        return null;
    }

    private String toUpperCase(final String str) {
        return Character.toString(str.charAt(0)).toUpperCase()+str.substring(1);
    }

    private String convertToString(final Object object) throws IllegalAccessException {
        if (object != null) {
            final String rowIdentifierAsString = object.toString();

            if (StringUtils.isNotEmpty(rowIdentifierAsString)) {
                return rowIdentifierAsString;
            }
        }

        return null;
    }

}
