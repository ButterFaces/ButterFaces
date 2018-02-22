package org.butterfaces.component.partrenderer;

import org.butterfaces.component.html.HtmlTooltip;
import org.butterfaces.component.html.text.HtmlText;
import org.butterfaces.util.FacesContextMockCreator;
import org.butterfaces.component.html.HtmlTooltip;
import org.butterfaces.component.html.text.HtmlText;
import org.junit.Test;

import javax.faces.context.FacesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TooltipValidationRendererTest {

    private final TooltipValidationRenderer renderer = new TooltipValidationRenderer();

    private final HtmlText uiComponent = new HtmlText();
    private final FacesContext facesContextMock = FacesContextMockCreator.createFacesContextCurrentInstance(uiComponent);

    @Test
    public void checkTooltipIsRenderedOnValidationErrorWithTooltipAlreadyExists() throws Exception {
        final HtmlTooltip tooltip = new HtmlTooltip();
        uiComponent.setValid(false);
        uiComponent.getChildren().add(tooltip);

        assertThat(uiComponent.getChildCount()).isEqualTo(1);

        renderer.renderTooltipIfNecessary(facesContextMock, uiComponent);

        // verify no additional tooltip is added
        assertThat(uiComponent.getChildCount()).isEqualTo(1);
        // in case of existing rendered tooltip no encoding will be triggered
        verifyNoMoreInteractions(facesContextMock.getResponseWriter());
    }

    @Test
    public void checkTooltipIsRenderedOnValidationErrorWithTooltipAlreadyExistsButNotRendered() throws Exception {
        final HtmlTooltip tooltip = new HtmlTooltip();
        tooltip.setRendered(false);
        uiComponent.setValid(false);
        uiComponent.getChildren().add(tooltip);

        assertThat(uiComponent.getChildCount()).isEqualTo(1);

        renderer.renderTooltipIfNecessary(facesContextMock, uiComponent);

        // verify no additional tooltip is added
        assertThat(uiComponent.getChildCount()).isEqualTo(1);
        // verify tooltip encoding will be triggered because existing tooltip is not rendered
        verify(facesContextMock).getResponseWriter();
        verify(facesContextMock.getResponseWriter()).writeAttribute("class", "butter-component-tooltip-temp-content", null);
    }

    @Test
    public void checkTooltipIsRenderedOnValidationErrorWithTooltipNotExists() throws Exception {
        uiComponent.setValid(false);

        assertThat(uiComponent.getChildCount()).isEqualTo(0);

        renderer.renderTooltipIfNecessary(facesContextMock, uiComponent);

        // verify no tooltip is added as child
        assertThat(uiComponent.getChildCount()).isEqualTo(0);
        // verify tooltip encoding will be triggered
        verify(facesContextMock).getResponseWriter();
        verify(facesContextMock.getResponseWriter()).writeAttribute("class", "butter-component-tooltip-temp-content", null);
    }
}