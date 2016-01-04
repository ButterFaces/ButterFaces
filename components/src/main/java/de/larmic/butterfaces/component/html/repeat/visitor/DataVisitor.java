/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.repeat.visitor;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Lars Michaelis
 */
public interface DataVisitor {

    DataVisitResult process(FacesContext context, Integer rowKey) throws IOException;

}
