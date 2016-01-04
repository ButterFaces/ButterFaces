/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.repeat.model;

import de.larmic.butterfaces.component.html.repeat.visitor.DataVisitor;
import org.junit.Test;

import javax.faces.model.DataModel;

import static org.mockito.Mockito.*;

/**
 * @author Lars Michaelis
 */
public class DataModelWrapperTest {

    @Test
    public void testWalk() throws Exception {
        final DataModel dataModelMock = mock(DataModel.class);
        final DataVisitor dataVisitorMock = mock(DataVisitor.class);

        final DataModelWrapper dataModelWrapper = new DataModelWrapper<>(dataModelMock);

        when(dataModelMock.getRowCount()).thenReturn(2);
        when(dataModelMock.isRowAvailable()).thenReturn(true);

        dataModelWrapper.walk(null, dataVisitorMock);

        verify(dataModelMock).setRowIndex(0);
        verify(dataModelMock).setRowIndex(1);

        verify(dataVisitorMock).process(null, 0);
        verify(dataVisitorMock).process(null, 1);

        verifyNoMoreInteractions(dataVisitorMock);
    }
}