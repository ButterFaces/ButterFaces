/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.table.export.model;

/**
 * A call back interface used by streaming expression bean.
 *
 * @author Lars Michaelis
 */
public interface StreamingCallback {

    /**
     * Data stream allows writing data on a writer.
     */
    DataStream getDataStream();

    /**
     * @return the stream encoding.
     */
    String getEncoding();

    /**
     * @return the file name without extension.
     */
    String getFilename();

    /**
     * @return the file name extension.
     */
    String getExtension();

}
