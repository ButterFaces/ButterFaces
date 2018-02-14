/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.renderkit.html_basic.action;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Lars Michaelis
 */
public class ParameterAppenderTest {

    @Test
    public void testAppendProperty() throws Exception {
        final StringBuilder builder = new StringBuilder();

        ParameterAppender.appendProperty(builder, "demo1", "ding1");
        assertThat(builder.toString()).isEqualTo("'demo1':'ding1'");

        ParameterAppender.appendProperty(builder, "demo2", "ding2");
        assertThat(builder.toString()).isEqualTo("'demo1':'ding1','demo2':'ding2'");

        builder.append(",{");
        ParameterAppender.appendProperty(builder, "demo3", "ding3");
        builder.append("}");
        assertThat(builder.toString()).isEqualTo("'demo1':'ding1','demo2':'ding2',{'demo3':'ding3'}");
    }
}