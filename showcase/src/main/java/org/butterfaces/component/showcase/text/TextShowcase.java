package org.butterfaces.component.showcase.text;

import org.butterfaces.component.showcase.AbstractInputShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.text.example.TextAutoTrimWebXmlExample;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.model.tree.EnumTreeBoxWrapper;
import org.butterfaces.util.StringUtils;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.JavaCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.component.showcase.text.example.TextAutoTrimWebXmlExample;
import org.butterfaces.component.showcase.type.StyleClass;
import org.butterfaces.util.StringUtils;

import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class TextShowcase extends AbstractInputShowcase implements Serializable {

    private FacetType selectedFacetType = FacetType.NONE;
    private String placeholder = DEFAULT_TEXT_PLACEHOLDER;
    private String type;
    private String pattern;
    private String min;
    private String max;
    private boolean autoFocus;
    private boolean useConverter;

    @Override
    protected Object initValue() {
        return null;
    }

    @Override
    public String getReadableValue() {
        if (this.getValue() != null) {
            return this.getValue().toString();
        }

        return "";
    }

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = this.createXhtmlCodeExample();

        codeExamples.add(xhtmlCodeExample);
        codeExamples.add(createMyBeanCodeExample());
        if (useConverter) {
            codeExamples.add(createUrlConverterCodeExample());
            codeExamples.add(createUrlBookmarkCodeExample());
        }

        generateDemoCSS(codeExamples);
        codeExamples.add(new TextAutoTrimWebXmlExample());
    }

    private XhtmlCodeExample createXhtmlCodeExample() {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:text id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                hideLabel=\"" + isHideLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                value=\"#{myBean.value}\"");
        xhtmlCodeExample.appendInnerContent("                placeholder=\"" + this.getPlaceholder() + "\"");
        xhtmlCodeExample.appendInnerContent("                type=\"" + this.getType() + "\"");
        xhtmlCodeExample.appendInnerContent("                pattern=\"" + this.getPattern() + "\"");
        xhtmlCodeExample.appendInnerContent("                min=\"" + this.getMin() + "\"");
        xhtmlCodeExample.appendInnerContent("                max=\"" + this.getMax() + "\"");
        if (this.getStyleClass() == StyleClass.BIG_LABEL) {
            xhtmlCodeExample.appendInnerContent("                styleClass=\"" + this.getSelectedStyleClass() + "\"");
        }
        xhtmlCodeExample.appendInnerContent("                readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                disabled=\"" + this.isDisabled() + "\"");
        xhtmlCodeExample.appendInnerContent("                autoFocus=\"" + this.isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        if (this.isValidation()) {
            xhtmlCodeExample.appendInnerContent("            <f:validateLength minimum=\"2\" maximum=\"10\"/>");
        }

        if (StringUtils.isNotEmpty(getTooltip())) {
            xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
            xhtmlCodeExample.appendInnerContent("                " + getTooltip());
            xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
        }

        if (useConverter) {
            xhtmlCodeExample.appendInnerContent("            <f:converter converterId=\"urlConverter\" />");
        }

        if (selectedFacetType == FacetType.INPUT_GROUP_ADDON) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-addon-left\">");
            xhtmlCodeExample.appendInnerContent("                Left input-group-addon");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-addon-right\">");
            xhtmlCodeExample.appendInnerContent("                Right input-group-addon");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        } else if (selectedFacetType == FacetType.INPUT_GROUP_BTN) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-btn-left\">");
            xhtmlCodeExample.appendInnerContent("                <button type=\"button\" class=\"btn btn-outline-secondary\">");
            xhtmlCodeExample.appendInnerContent("                     Go!");
            xhtmlCodeExample.appendInnerContent("                 </button>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"input-group-btn-right\">");
            xhtmlCodeExample.appendInnerContent("                <button type=\"button\" class=\"btn btn-outline-secondary\"");
            xhtmlCodeExample.appendInnerContent("                        dropdown-toggle\"");
            xhtmlCodeExample.appendInnerContent("                        data-toggle=\"dropdown\"");
            xhtmlCodeExample.appendInnerContent("                        aria-expanded=\"false\">");
            xhtmlCodeExample.appendInnerContent("                     Action <span class=\"caret\"></span>");
            xhtmlCodeExample.appendInnerContent("                 </button>");
            xhtmlCodeExample.appendInnerContent("                 <ul class=\"dropdown-menu dropdown-menu-right\" role=\"menu\">");
            xhtmlCodeExample.appendInnerContent("                     <li><a href=\"#\">Action</a></li>");
            xhtmlCodeExample.appendInnerContent("                     <li><a href=\"#\">Another action</a></li>");
            xhtmlCodeExample.appendInnerContent("                     <li><a href=\"#\">Something else here</a></li>");
            xhtmlCodeExample.appendInnerContent("                     <li class=\"divider\"></li>");
            xhtmlCodeExample.appendInnerContent("                     <li><a href=\"#\">Separated link</a></li>");
            xhtmlCodeExample.appendInnerContent("                 </ul>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        }

        xhtmlCodeExample.appendInnerContent("        </b:text>", false);

        this.addOutputExample(xhtmlCodeExample);

        return xhtmlCodeExample;
    }

    private JavaCodeExample createMyBeanCodeExample() {
        final JavaCodeExample myBean = new JavaCodeExample("MyBean.java", "myBean", "text.demo", "MyBean", true);

        if (useConverter) {
            myBean.appendInnerContent("   private URLBookmark value;\n");
            myBean.appendInnerContent("   public URLBookmark getValue() {");
            myBean.appendInnerContent("       return value;");
            myBean.appendInnerContent("   }");
            myBean.appendInnerContent("   public void setValue(URLBookmark value) {");
            myBean.appendInnerContent("       this.value = value;");
            myBean.appendInnerContent("   }");
        } else {
            myBean.appendInnerContent("   private String value;\n");
            myBean.appendInnerContent("   public String getValue() {");
            myBean.appendInnerContent("       return value;");
            myBean.appendInnerContent("   }");
            myBean.appendInnerContent("   public void setValue(String value) {");
            myBean.appendInnerContent("       this.value = value;");
            myBean.appendInnerContent("   }");
        }

        return myBean;
    }

    private JavaCodeExample createUrlBookmarkCodeExample() {
        final JavaCodeExample myBean = new JavaCodeExample("URLBookmark.java", "uRLBookmark", "text.demo", "URLBookmark", false);

        myBean.appendInnerContent("   private String fullURL;\n");
        myBean.appendInnerContent("   public URLBookmark(String fullURL) {");
        myBean.appendInnerContent("       this.fullURL = fullURL;");
        myBean.appendInnerContent("   }");
        myBean.appendInnerContent("   public String getFullURL() {");
        myBean.appendInnerContent("       return fullURL;");
        myBean.appendInnerContent("   }");
        myBean.appendInnerContent("   public void setFullURL(String fullURL) {");
        myBean.appendInnerContent("       this.fullURL = fullURL;");
        myBean.appendInnerContent("   }");
        myBean.appendInnerContent("   public String toString(){");
        myBean.appendInnerContent("       return fullURL;");
        myBean.appendInnerContent("   }");

        return myBean;
    }

    private JavaCodeExample createUrlConverterCodeExample() {
        final JavaCodeExample myBean = new JavaCodeExample("UrlConverter.java", "urlConverter", "text.demo", "UrlConverter", false, "@FacesConverter(\"urlConverter\")");

        myBean.addImport("org.apache.commons.validator.routines.UrlValidator");
        myBean.addImport("\njavax.faces.application.FacesMessage");
        myBean.addImport("javax.faces.component.UIComponent");
        myBean.addImport("javax.faces.context.FacesContext");
        myBean.addImport("javax.faces.convert.Converter");
        myBean.addImport("javax.faces.convert.ConverterException");
        myBean.addImport("javax.faces.convert.FacesConverter");

        myBean.addInterfaces("Converter");

        myBean.appendInnerContent("   public static final String HTTP = \"http://\";\n");
        myBean.appendInnerContent("   @Override");
        myBean.appendInnerContent("   public Object getAsObject(final FacesContext context,");
        myBean.appendInnerContent("                             final UIComponent component,");
        myBean.appendInnerContent("                             final String value) {");
        myBean.appendInnerContent("      final StringBuilder url = new StringBuilder();\n");
        myBean.appendInnerContent("      this.appendHttpIfNecessary(value, url);\n");
        myBean.appendInnerContent("      //use Apache common URL validator to validate URL");
        myBean.appendInnerContent("      if(!new UrlValidator().isValid(url.toString())){");
        myBean.appendInnerContent("          final FacesMessage msg = new FacesMessage(\"URL Conversion error.\", \"Invalid URL format.\");");
        myBean.appendInnerContent("          msg.setSeverity(FacesMessage.SEVERITY_ERROR);");
        myBean.appendInnerContent("          throw new ConverterException(msg);");
        myBean.appendInnerContent("      }\n");
        myBean.appendInnerContent("      return new URLBookmark(url.toString());");
        myBean.appendInnerContent("   }\n");
        myBean.appendInnerContent("   @Override");
        myBean.appendInnerContent("   public String getAsString(final FacesContext context,");
        myBean.appendInnerContent("                             final UIComponent component,");
        myBean.appendInnerContent("                             final Object value) {");
        myBean.appendInnerContent("      return value.toString();");
        myBean.appendInnerContent("   }\n");
        myBean.appendInnerContent("   private void appendHttpIfNecessary(final String value,");
        myBean.appendInnerContent("                                      final StringBuilder url) {");
        myBean.appendInnerContent("      if(!value.startsWith(HTTP, 0)){");
        myBean.appendInnerContent("          url.append(HTTP);");
        myBean.appendInnerContent("      }");
        myBean.appendInnerContent("      url.append(value);");
        myBean.appendInnerContent("   }");

        return myBean;
    }

    public void someListenerAction(AjaxBehaviorEvent event) {

    }

    public List<EnumTreeBoxWrapper> getAvailableFacetTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final FacetType type : FacetType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder(final String placeholder) {
        this.placeholder = placeholder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public FacetType getSelectedFacetType() {
        return selectedFacetType;
    }

    public void setSelectedFacetType(FacetType selectedFacetType) {
        this.selectedFacetType = selectedFacetType;
        if (this.selectedFacetType != FacetType.NONE) {
            this.useConverter = false;
        }
    }

    public boolean isUseConverter() {
        return useConverter;
    }

    public void setUseConverter(boolean useConverter) {
        this.useConverter = useConverter;
        this.selectedFacetType = FacetType.NONE;
    }
}
