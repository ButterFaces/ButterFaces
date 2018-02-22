/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package org.butterfaces.component.html.repeat.model;

import org.butterfaces.component.html.repeat.visitor.DataVisitResult;
import org.butterfaces.component.html.repeat.visitor.DataVisitor;
import org.butterfaces.component.html.repeat.visitor.DataVisitor;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import java.io.IOException;

/**
 * @author Lars Michaelis
 */
public class DataModelWrapper<E> extends DataModel<E> {

    private DataModel<E> wrappedModel;

    public DataModelWrapper(DataModel<E> wrapped) {
        this.wrappedModel = wrapped;
    }

    public void walk(FacesContext context, DataVisitor visitor) throws IOException {
        int rowCount = wrappedModel.getRowCount();
        int currentRow = 0;

        while (rowCount < 0 || currentRow < rowCount) {
            wrappedModel.setRowIndex(currentRow);

            if (!wrappedModel.isRowAvailable() || DataVisitResult.STOP.equals(visitor.process(context, currentRow))) {
                break;
            }

            currentRow++;
        }
    }

    public int getRowCount() {
        return wrappedModel.getRowCount();
    }

    public E getRowData() {
        return wrappedModel.getRowData();
    }

    public int getRowIndex() {
        return wrappedModel.getRowIndex();
    }

    public Object getWrappedData() {
        return wrappedModel.getWrappedData();
    }

    public boolean isRowAvailable() {
        return wrappedModel.isRowAvailable();
    }

    public void setRowIndex(int rowIndex) {
        wrappedModel.setRowIndex(rowIndex);
    }

    public void setWrappedData(Object data) {
        wrappedModel.setWrappedData(data);
    }
}
