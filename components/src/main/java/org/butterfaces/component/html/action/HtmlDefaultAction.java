/**
 * Copyright 2012 Lars Michaelis
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.butterfaces.component.html.action;

import javax.faces.component.FacesComponent;

/**
 * A concrete implementation of {@link AbstractKeyCodeAction}.
 * The enter button (key code 13) is the listing key code that will be fire a
 * click event on ambient component.
 */
@FacesComponent(HtmlDefaultAction.COMPONENT_TYPE)
public class HtmlDefaultAction extends AbstractKeyCodeAction {

	public static final String COMPONENT_TYPE = "org.butterfaces.component.defaultAction";
	private static final String DEFAULT_ACTION_MARKER = "org.butterfaces.component.DefaultAction";

	@Override
	public String getListeningKeyCode() {
		return "13";
	}

	@Override
	public String getFormActionMarker() {
		return DEFAULT_ACTION_MARKER;
	}

}
