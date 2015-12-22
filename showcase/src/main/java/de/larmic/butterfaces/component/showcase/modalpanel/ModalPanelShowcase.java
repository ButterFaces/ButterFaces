package de.larmic.butterfaces.component.showcase.modalpanel;

import de.larmic.butterfaces.component.showcase.AbstractCodeShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.JavaCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class ModalPanelShowcase extends AbstractCodeShowcase implements Serializable {

    private ModalPanelType modalPanelType = ModalPanelType.SIMPLE;
    private String title = "Demo title";
    private String cancelButtonText;
    private String text;

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        if (modalPanelType == ModalPanelType.SIMPLE) {
            xhtmlCodeExample.appendInnerContent("\n        <b:modalPanel id=\"modalPanel\"");
            xhtmlCodeExample.appendInnerContent("                      cancelButtonText=\"" + cancelButtonText + "\"");
            xhtmlCodeExample.appendInnerContent("                      title=\"" + title + "\">");
            xhtmlCodeExample.appendInnerContent("            Lorem ipsum dolor sit amet, consectetuer ...");
            xhtmlCodeExample.appendInnerContent("        </b:modalPanel>\n");
            xhtmlCodeExample.appendInnerContent("        <span onClick=\"butter.modal.open('modalPanel');\"");
            xhtmlCodeExample.appendInnerContent("              class=\"btn btn-default\">");
            xhtmlCodeExample.appendInnerContent("            open modal panel");
            xhtmlCodeExample.appendInnerContent("        </span>");
        } else if (modalPanelType == ModalPanelType.CUSTOM_HEADER) {
            xhtmlCodeExample.appendInnerContent("\n        <b:modalPanel id=\"modalPanel\"");
            xhtmlCodeExample.appendInnerContent("                      cancelButtonText=\"" + cancelButtonText + "\">");
            xhtmlCodeExample.appendInnerContent("            Lorem ipsum dolor sit amet, consectetuer ...");
            xhtmlCodeExample.appendInnerContent("            <!-- using header facet will ignore title attribute -->");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"header\">");
            xhtmlCodeExample.appendInnerContent("               <div>");
            xhtmlCodeExample.appendInnerContent("                  <strong>Warning: </strong>" + title + "");
            xhtmlCodeExample.appendInnerContent("               </div>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
            xhtmlCodeExample.appendInnerContent("        </b:modalPanel>\n");
            xhtmlCodeExample.appendInnerContent("        <span onClick=\"butter.modal.open('modalPanel');\"");
            xhtmlCodeExample.appendInnerContent("              class=\"btn btn-default\">");
            xhtmlCodeExample.appendInnerContent("            open modal panel");
            xhtmlCodeExample.appendInnerContent("        </span>");
        } else if (modalPanelType == ModalPanelType.CUSTOM_FOOTER) {
            xhtmlCodeExample.appendInnerContent("\n        <b:modalPanel id=\"modalPanel\"");
            xhtmlCodeExample.appendInnerContent("                      cancelButtonText=\"" + cancelButtonText + "\"");
            xhtmlCodeExample.appendInnerContent("                      title=\"" + title + "\">");
            xhtmlCodeExample.appendInnerContent("            Lorem ipsum dolor sit amet, consectetuer ...");
            xhtmlCodeExample.appendInnerContent("            <!-- when using footer facet additional-footer will be ignored -->");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"footer\">");
            xhtmlCodeExample.appendInnerContent("                <span onClick=\"butter.modal.close('customFooterModalPanel');\"");
            xhtmlCodeExample.appendInnerContent("                      class=\"btn btn-default\">");
            xhtmlCodeExample.appendInnerContent("                    close modal panel");
            xhtmlCodeExample.appendInnerContent("                </span>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
            xhtmlCodeExample.appendInnerContent("        </b:modalPanel>\n");
            xhtmlCodeExample.appendInnerContent("        <span onClick=\"butter.modal.open('modalPanel');\"");
            xhtmlCodeExample.appendInnerContent("              class=\"btn btn-default\">");
            xhtmlCodeExample.appendInnerContent("            open modal panel");
            xhtmlCodeExample.appendInnerContent("        </span>");
        } else if (modalPanelType == ModalPanelType.COMPLEX) {
            xhtmlCodeExample.appendInnerContent("\n        <b:modalPanel id=\"modalPanel\"");
            xhtmlCodeExample.appendInnerContent("                      cancelButtonText=\"" + cancelButtonText + "\"");
            xhtmlCodeExample.appendInnerContent("                      title=\"" + title + "\">");
            xhtmlCodeExample.appendInnerContent("            <b:text id=\"requiredInput\"");
            xhtmlCodeExample.appendInnerContent("                    value=\"#{myBean.text}\"");
            xhtmlCodeExample.appendInnerContent("                    required=\"true\"");
            xhtmlCodeExample.appendInnerContent("                    label=\"Required input\"/>");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"additional-footer\">");
            xhtmlCodeExample.appendInnerContent("                <b:commandLink value=\"Save\"");
            xhtmlCodeExample.appendInnerContent("                               action=\"#{myBean.submit}\"");
            xhtmlCodeExample.appendInnerContent("                               ajaxDisableRenderRegionsOnRequest=\"false\"");
            xhtmlCodeExample.appendInnerContent("                               styleClass=\"btn btn-success\">");
            xhtmlCodeExample.appendInnerContent("                    <f:ajax execute=\"requiredInput\"");
            xhtmlCodeExample.appendInnerContent("                            render=\"requiredInput closeModalPanelScriplet\"");
            xhtmlCodeExample.appendInnerContent("                            onevent=\"closeIfNoValidationErrorOccured\"/>");
            xhtmlCodeExample.appendInnerContent("                </b:commandLink>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
            xhtmlCodeExample.appendInnerContent("        </b:modalPanel>\n");
            xhtmlCodeExample.appendInnerContent("        <h:panelGroup id=\"closeModalPanelScriplet\">");
            xhtmlCodeExample.appendInnerContent("            <script type=\"text/javascript\">");
            xhtmlCodeExample.appendInnerContent("                //<![CDATA[");
            xhtmlCodeExample.appendInnerContent("                function closeIfNoValidationErrorOccured(data) {");
            xhtmlCodeExample.appendInnerContent("                    // custom validation (mostly check faces context)");
            xhtmlCodeExample.appendInnerContent("                    var jsfValidation = '#{facesContext.maximumSeverity != null}';");
            xhtmlCodeExample.appendInnerContent("                    var validationFailed = jsfValidation === 'true';");
            xhtmlCodeExample.appendInnerContent("                    if (data.status == 'success' && !validationFailed) {");
            xhtmlCodeExample.appendInnerContent("                        butter.modal.close('modalPanel');");
            xhtmlCodeExample.appendInnerContent("                    }");
            xhtmlCodeExample.appendInnerContent("                }");
            xhtmlCodeExample.appendInnerContent("                //]]>");
            xhtmlCodeExample.appendInnerContent("            </script>");
            xhtmlCodeExample.appendInnerContent("        </h:panelGroup>\n");
            xhtmlCodeExample.appendInnerContent("        <span onClick=\"butter.modal.open('modalPanel');\"");
            xhtmlCodeExample.appendInnerContent("              class=\"btn btn-default\">");
            xhtmlCodeExample.appendInnerContent("            open modal panel");
            xhtmlCodeExample.appendInnerContent("        </span>");
        }

        codeExamples.add(xhtmlCodeExample);

        if (modalPanelType == ModalPanelType.COMPLEX) {
            final JavaCodeExample javaCodeExample = new JavaCodeExample("MyBean.java", "mybean", "modal.panel.demo", "MyBean", true);

            javaCodeExample.appendInnerContent("    private final String text;\n");
            javaCodeExample.appendInnerContent("    public void submit() {");
            javaCodeExample.appendInnerContent("        // do something");
            javaCodeExample.appendInnerContent("    }\n");
            javaCodeExample.appendInnerContent("    // getter and setter");

            codeExamples.add(javaCodeExample);
        }
    }

    public void submit() {

    }

    public List<SelectItem> getModalPanelTypes() {
        final List<SelectItem> items = new ArrayList<>();

        for (final ModalPanelType type : ModalPanelType.values()) {
            items.add(new SelectItem(type, type.label));
        }
        return items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCancelButtonText() {
        return cancelButtonText;
    }

    public void setCancelButtonText(String cancelButtonText) {
        this.cancelButtonText = cancelButtonText;
    }

    public ModalPanelType getModalPanelType() {
        return modalPanelType;
    }

    public void setModalPanelType(ModalPanelType modalPanelType) {
        this.modalPanelType = modalPanelType;
    }
}
