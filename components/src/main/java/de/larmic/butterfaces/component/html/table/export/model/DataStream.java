/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table.export.model;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Lars Michaelis
 */
public interface DataStream {

    void writeData(final OutputStream outputStream) throws IOException, JAXBException, TransformerException, ParserConfigurationException;

    boolean isReady();

    boolean isResultsAvailable();

}