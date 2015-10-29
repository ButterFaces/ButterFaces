package de.larmic.butterfaces.component.showcase.comboBox;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.LinkedHashMap;
import java.util.Map;

@FacesConverter("fooConverter")
public class FooConverter implements Converter {
	public static Map<String, Foo> fooMap;

	static {
		fooMap = new LinkedHashMap<>();
		fooMap.put("fooKey1", new Foo("fooKey1", "fooValue1"));
		fooMap.put("fooKey2", new Foo("fooKey2", "fooValue2"));
		fooMap.put("fooKey3", new Foo("fooKey3", "fooValue3"));
	}

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		return fooMap.get(value);
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value instanceof Foo) {
			return ((Foo) value).getKey();
		}

		return null;
	}
}
