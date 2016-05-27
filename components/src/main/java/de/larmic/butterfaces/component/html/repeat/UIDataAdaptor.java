/*
 * Copyright Lars Michaelis and Stephan Zerhusen 2016.
 * Distributed under the MIT License.
 * (See accompanying file README.md file or copy at http://opensource.org/licenses/MIT)
 */
package de.larmic.butterfaces.component.html.repeat;

import de.larmic.butterfaces.component.html.repeat.event.RowKeyEventBroadcaster;
import de.larmic.butterfaces.component.html.repeat.event.RowKeyFacesEvent;
import de.larmic.butterfaces.component.html.repeat.model.DataModelWrapper;
import de.larmic.butterfaces.component.html.repeat.model.DataModelWrapperFactory;
import de.larmic.butterfaces.component.html.repeat.visitor.ChildrenComponentVisitor;
import de.larmic.butterfaces.component.html.repeat.visitor.ChildrenTreeDataVisitor;
import de.larmic.butterfaces.component.html.repeat.visitor.ChildrenTreeDataVisitorCallback;
import de.larmic.butterfaces.component.html.repeat.visitor.DataVisitor;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.*;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Lars Michaelis
 */
public abstract class UIDataAdaptor extends UIComponentBase implements NamingContainer, UniqueIdVendor, ComponentSystemEventListener, SystemEventListener, ChildrenTreeDataVisitorCallback {

    private String PRE_RENDER_VIEW_EVENT_REGISTERED = UIDataAdaptor.class.getName() + ":preRenderViewEventRegistered";

    private static final Logger LOG = Logger.getLogger(UIDataAdaptor.class.getName());

    protected final char separatorChar;

    private DataModelWrapper<?> dataModelWrapper = null;
    private Integer rowKey = null;
    private String containerClientId;
    Stack<Object> originalVarValues = new Stack<>();

    private enum PropertyKeys {
        lastId, var, stateVar, childState, first, rows, value, status
    }

    public UIDataAdaptor() {
        this.subscribeToEvent(PostAddToViewEvent.class, this);
        this.subscribeToEvent(PostRestoreStateEvent.class, this);

        separatorChar = UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance());
    }

    public String createUniqueId(FacesContext context, String seed) {
        Integer i = (Integer) getStateHelper().get(PropertyKeys.lastId);
        int lastId = (i != null) ? i : 0;

        getStateHelper().put(PropertyKeys.lastId, ++lastId);

        return UIViewRoot.UNIQUE_ID_PREFIX + ((seed == null) ? lastId : seed);
    }

    public Integer getRowKey() {
        return rowKey;
    }

    @Override
    public void setRowKey(FacesContext facesContext, Integer rowKey) {
        this.saveChildState(facesContext);

        this.rowKey = rowKey;
        final int rowKeyAsInt = rowKey != null ? rowKey : -1;

        getDataModelWrapper().setRowIndex(rowKeyAsInt);

        this.containerClientId = null;

        boolean rowSelected = (rowKey != null) && isRowAvailable();

        setupVariable(facesContext, rowSelected);

        this.restoreChildState(facesContext);
    }

    @Override
    public void queueEvent(FacesEvent event) {
        super.queueEvent(wrapEvent(event));
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof RowKeyFacesEvent) {
            RowKeyEventBroadcaster.broadcast(getFacesContext(), (RowKeyFacesEvent) event);
        } else {
            super.broadcast(event);
        }
    }

    private Map<String, Object> getVariablesMap(FacesContext facesContext) {
        return facesContext.getExternalContext().getRequestMap();
    }

    private void saveChildState(FacesContext facesContext) {
        final Iterator<UIComponent> itr = dataChildren();

        while (itr.hasNext()) {
            this.saveChildState(facesContext, itr.next());
        }
    }

    private void saveChildState(FacesContext facesContext, UIComponent component) {
        if (component.isTransient()) {
            return;
        }

        ChildStateHolder childStateHolder = null;

        if (component instanceof EditableValueHolder) {
            EditableValueHolder evh = (EditableValueHolder) component;

            childStateHolder = new ChildStateHolder(evh);
        } else if (component instanceof UIForm) {
            UIForm form = (UIForm) component;

            childStateHolder = new ChildStateHolder(form);
        }

        if (childStateHolder != null) {
            getStateHelper().put(PropertyKeys.childState, component.getClientId(facesContext), childStateHolder);
        }

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                saveChildState(facesContext, child);
            }
        }

        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                saveChildState(facesContext, facet);
            }
        }
    }

    public Iterator<UIComponent> dataChildren() {
        if (getChildCount() > 0) {
            return getChildren().iterator();
        } else {
            return Collections.<UIComponent>emptyList().iterator();
        }
    }

    private void restoreChildState(FacesContext facesContext) {
        Iterator<UIComponent> itr = dataChildren();

        while (itr.hasNext()) {
            this.restoreChildState(facesContext, itr.next());
        }
    }

    private void restoreChildState(FacesContext facesContext, UIComponent component) {
        String id = component.getId();

        component.setId(id); // Forces client id to be reset

        ChildStateHolder childStateHolder = null;
        Map<String, ChildStateHolder> savedStatesMap = (Map<String, ChildStateHolder>) getStateHelper().get(PropertyKeys.childState);

        if (savedStatesMap != null) {
            childStateHolder = savedStatesMap.get(component.getClientId(facesContext));
        }

        if (childStateHolder == null) {
            childStateHolder = ChildStateHolder.EMPTY;
        }

        if (component instanceof EditableValueHolder) {
            EditableValueHolder evh = (EditableValueHolder) component;

            childStateHolder.apply(evh);
        } else if (component instanceof UIForm) {
            UIForm form = (UIForm) component;

            childStateHolder.apply(form);
        }

        if (component.getChildCount() > 0) {
            for (UIComponent child : component.getChildren()) {
                restoreChildState(facesContext, child);
            }
        }

        if (component.getFacetCount() > 0) {
            for (UIComponent facet : component.getFacets().values()) {
                restoreChildState(facesContext, facet);
            }
        }
    }

    private FacesEvent wrapEvent(FacesEvent event) {
        return new RowKeyFacesEvent(this, event, getRowKey());
    }

    private DataModelWrapper<?> getDataModelWrapper() {
        if (dataModelWrapper == null) {
            dataModelWrapper = DataModelWrapperFactory.createDataModelWrapper(getValue());
        }

        return dataModelWrapper;
    }

    public int getRowIndex() {
        return getDataModelWrapper().getRowIndex();
    }

    public String getVar() {
        return (String) getStateHelper().get(PropertyKeys.var);
    }

    public void setVar(String var) {
        getStateHelper().put(PropertyKeys.var, var);
    }

    public Object getValue() {
        return getStateHelper().eval(PropertyKeys.value);
    }

    public void setValue(Object value) {
        resetDataModel();
        getStateHelper().put(PropertyKeys.value, value);
    }

    public String getStatus() {
        return (String) getStateHelper().get(PropertyKeys.status);
    }

    public void setStatus(String status) {
        getStateHelper().put(PropertyKeys.status, status);
    }

    public int getRowCount() {
        return getDataModelWrapper().getRowCount();
    }

    public Object getRowData() {
        return getDataModelWrapper().getRowData();
    }

    public boolean isRowAvailable() {
        return getDataModelWrapper().isRowAvailable();
    }

    private void setupVariable(FacesContext faces, boolean rowSelected) {
        Map<String, Object> attrs = getVariablesMap(faces);

        if (rowSelected) {
            setupVariable(getVar(), attrs, getRowData());
        } else {
            removeVariable(getVar(), attrs);
        }

        String iterationStatusVar = getStatus();
        if (iterationStatusVar != null) {
            Map<String, Object> requestMap = getVariablesMap(faces);

            if (rowSelected) {
                RowStatus iterationStatus = new RowStatus(getRowIndex(), getRowCount());
                requestMap.put(iterationStatusVar, iterationStatus);
            } else {
                requestMap.remove(iterationStatusVar);
            }
        }
    }

    private void setupVariable(String var, Map<String, Object> attrs, Object rowData) {
        if (var != null) {
            attrs.put(var, rowData);
        }
    }

    private void removeVariable(String var, Map<String, Object> attrs) {
        if (var != null) {
            attrs.remove(var);
        }
    }

    @Override
    public String getContainerClientId(FacesContext facesContext) {
        if (facesContext == null) {
            throw new NullPointerException("context");
        }

        if (null == containerClientId) {
            containerClientId = super.getContainerClientId(facesContext);

            final Integer rowKey = getRowKey();

            if (rowKey != null) {
                containerClientId = containerClientId + separatorChar + rowKey;
                // Using StringJoiner is to slow
                //containerClientId = StringJoiner.on(separatorChar).join(Arrays.asList(containerClientId, rowKey.toString())).toString();
            }
        }

        return containerClientId;
    }

    public void restoreOrigValue(FacesContext faces) {
        String var = getVar();

        if (var != null) {
            final Map<String, Object> attrs = getVariablesMap(faces);

            if (!this.originalVarValues.isEmpty()) {
                attrs.put(var, this.originalVarValues.pop());
            } else {
                attrs.remove(var);
            }
        }

        String iterationStatusVar = getStatus();
        if (iterationStatusVar != null) {
            Map<String, Object> variablesMap = getVariablesMap(faces);
            variablesMap.remove(iterationStatusVar);
        }
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("value".equals(name)) {
            resetDataModel();
        }

        if ("var".equals(name) || "rowKeyVar".equals(name) || "stateVar".equals(name)) {
            throw new IllegalArgumentException(MessageFormat.format("{0} cannot be EL-expression", name));
        }

        super.setValueExpression(name, binding);
    }

    private boolean keepSaved(FacesContext context) {
        final FacesMessage.Severity maximumSeverity = context.getMaximumSeverity();
        return (maximumSeverity != null) && (FacesMessage.SEVERITY_ERROR.compareTo(maximumSeverity) <= 0);
    }

    public void walk(FacesContext faces, DataVisitor visitor) throws IOException {
        getDataModelWrapper().walk(faces, visitor);
        restoreOrigValue(faces);
    }

    @Override
    public void setId(String id) {
        super.setId(id);
        this.containerClientId = null;
    }

    public void resetDataModel() {
        this.dataModelWrapper = null;
    }

    protected void resetChildState() {
        getStateHelper().remove(PropertyKeys.childState);
    }

    private void resetState() {
        this.dataModelWrapper = null;

        if (!keepSaved(getFacesContext())) {
            resetChildState();
        }
    }

    @Override
    public Object saveState(FacesContext context) {
        final Object parentState = super.saveState(context);

        if (initialStateMarked() && parentState == null) {
            return null;
        }

        return new Object[]{parentState};
    }

    @Override
    public void restoreState(FacesContext context, Object stateObject) {
        if (stateObject == null) {
            return;
        }

        super.restoreState(context, ((Object[]) stateObject)[0]);
    }

    private boolean matchesBaseId(String clientId, String baseId, char separatorChar) {
        return clientId.equals(baseId)
                || clientId.startsWith(baseId)
                && (clientId.length() > baseId.length())
                && (clientId.charAt(baseId.length()) == separatorChar);

    }

    @Override
    public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback) throws FacesException {
        if ((null == context) || (null == clientId) || (null == callback)) {
            throw new NullPointerException();
        }

        final String baseId = getClientId(context);

        if (!matchesBaseId(clientId, baseId, separatorChar)) {
            return false;
        }

        boolean found = false;
        Integer oldRowKey = getRowKey();

        try {
            if (clientId.equals(baseId)) {
                callback.invokeContextCallback(context, this);
                found = true;
            }

            if (!found) {
                setRowKey(context, null);

                if (isRowAvailable()) {
                    Iterator<UIComponent> dataChildrenItr = dataChildren();

                    while (dataChildrenItr.hasNext() && !found) {
                        UIComponent dataChild = dataChildrenItr.next();

                        found = dataChild.invokeOnComponent(context, clientId, callback);
                    }
                }
            }
        } catch (Exception e) {
            throw new FacesException(e);
        } finally {
            try {
                setRowKey(context, oldRowKey);
                restoreOrigValue(context);
            } catch (Exception e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }

        return found;
    }

    private boolean visitComponents(Iterator<UIComponent> components, VisitContext context, VisitCallback callback) {

        while (components.hasNext()) {
            UIComponent nextChild = components.next();

            if (nextChild.visitTree(context, callback)) {
                return true;
            }
        }

        return false;
    }

    protected boolean visitDataChildren(VisitContext visitContext, VisitCallback callback, boolean visitRows) throws IOException {
        if (visitRows) {
            final FacesContext facesContext = visitContext.getFacesContext();

            final ChildrenTreeDataVisitor dataVisitor = new ChildrenTreeDataVisitor(callback, visitContext, this);
            this.walk(facesContext, dataVisitor);
            return dataVisitor.getVisitResult();
        } else {
            return visitComponents(getFacetsAndChildren(), visitContext, callback);
        }
    }

    /**
     * Copied from Richfaces UIDataAdapter#visitTree.
     */
    @Override
    public boolean visitTree(VisitContext visitContext, VisitCallback callback) {

        // First check to see whether we are visitable. If not
        // short-circuit out of this subtree, though allow the
        // visit to proceed through to other subtrees.
        if (!isVisitable(visitContext)) {
            return false;
        }

        // Clear out the row index is one is set so that
        // we start from a clean slate.
        FacesContext facesContext = visitContext.getFacesContext();

        // NOTE: that the visitRows local will be obsolete once the
        // appropriate visit hints have been added to the API
        boolean visitRows = requiresRowIteration(visitContext);

        Integer oldRowKey = null;
        if (visitRows) {
            oldRowKey = getRowKey();
            setRowKey(facesContext, null);
        }

        // Push ourselves to EL
        pushComponentToEL(facesContext, null);

        try {

            // Visit ourselves. Note that we delegate to the
            // VisitContext to actually perform the visit.
            VisitResult result = visitContext.invokeVisitCallback(this, callback);

            // If the visit is complete, short-circuit out and end the visit
            if (result == VisitResult.COMPLETE) {
                return true;
            }

            // Visit children, short-circuiting as necessary
            if ((result == VisitResult.ACCEPT)) {
                if (visitDataChildren(visitContext, callback, visitRows)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // TODO handle exception
            LOG.log(Level.SEVERE, e.getMessage(), e);
        } finally {

            // Clean up - pop EL and restore old row index
            popComponentFromEL(facesContext);

            if (visitRows) {
                try {
                    setRowKey(facesContext, oldRowKey);
                    restoreOrigValue(facesContext);
                } catch (Exception e) {

                    // TODO: handle exception
                    LOG.log(Level.SEVERE, e.getMessage(), e);
                }
            }
        }

        // Return false to allow the visit to continue
        return false;
    }

    private boolean requiresRowIteration(VisitContext context) {
        return !context.getHints().contains(VisitHint.SKIP_ITERATION);
    }

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        this.processEvent((SystemEvent) event);
    }

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PostAddToViewEvent) {
            subscribeToPreRenderViewEventOncePerRequest();
        } else if (event instanceof PostRestoreStateEvent) {
            subscribeToPreRenderViewEventOncePerRequest();
            resetState();
        } else if (event instanceof PreRenderViewEvent) {
            resetState();
        }
    }

    @Override
    public void processDecodes(FacesContext faces) {
        if (!this.isRendered()) {
            return;
        }

        pushComponentToEL(faces, this);

        this.walkThroughChildren(faces, new ChildrenComponentVisitor(this) {
            @Override
            public void processComponent(FacesContext context, UIComponent component) {
                component.processDecodes(context);
            }
        });

        this.decode(faces);
        popComponentFromEL(faces);
    }

    private void walkThroughChildren(FacesContext faces, ChildrenComponentVisitor visitor) {
        if (!this.isRendered()) {
            return;
        }

        final String var = getVar();
        if (var != null) {
            Map<String, Object> attrs = getVariablesMap(faces);

            this.originalVarValues.push(attrs.get(var));
        }
        this.setRowKey(faces, null);

        try {
            walk(faces, visitor);
        } catch (Exception e) {
            throw new FacesException(e);
        } finally {
            this.setRowKey(faces, null);
            this.restoreOrigValue(faces);
        }
    }

    private void subscribeToPreRenderViewEventOncePerRequest() {
        final FacesContext facesContext = getFacesContext();
        final Map<Object, Object> contextMap = facesContext.getAttributes();
        if (contextMap.get(this.getClientId() + PRE_RENDER_VIEW_EVENT_REGISTERED) == null) {
            contextMap.put(this.getClientId() + PRE_RENDER_VIEW_EVENT_REGISTERED, Boolean.TRUE);
            UIViewRoot viewRoot = facesContext.getViewRoot();
            viewRoot.subscribeToViewEvent(PreRenderViewEvent.class, this);
        }
    }

    @Override
    public boolean isListenerForSource(Object source) {
        return this.equals(source) || source instanceof UIViewRoot;
    }
}
