package de.larmic.jsf2.component.showcase;

import java.io.Serializable;
import java.util.Iterator;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
@SuppressWarnings("serial")
public class ClientId implements Serializable {

	public String find(final String id) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final UIComponent root = context.getViewRoot();

		final UIComponent component = this.find(root, id);

		return component != null ? component.getClientId() : "";
	}

	private UIComponent find(final UIComponent root, final String id) {

		UIComponent result = null;

		if (root.getId() != null && root.getId().equals(id)) {
			result = root;
		}

		UIComponent kid = null;

		final Iterator<UIComponent> kids = root.getFacetsAndChildren();

		while (kids.hasNext() && result == null) {
			kid = kids.next();

			if (kid.getId() != null && kid.getId().equals(id)) {
				result = kid;
				break;
			}

			result = this.find(kid, id);

		}

		return result;
	}
}
