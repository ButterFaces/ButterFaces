package de.larmic.butterfaces.component.showcase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import de.larmic.butterfaces.component.showcase.comboBox.Foo;
import de.larmic.butterfaces.component.showcase.comboBox.FooConverter;
import de.larmic.butterfaces.component.showcase.comboBox.FooType;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.type.ComboBoxValueType;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class ComboBoxShowcase extends AbstractInputShowcase implements Serializable {

	private ComboBoxValueType comboBoxValueType = ComboBoxValueType.STRING;

	private boolean filterable;
    private boolean autoFocus;

	private final List<SelectItem> foos = new ArrayList<SelectItem>();
	private final List<SelectItem> enums = new ArrayList<SelectItem>();
	private final List<SelectItem> strings = new ArrayList<SelectItem>();

	public ComboBoxShowcase() {
		this.initFoos();
		this.initStrings();
		this.initEnums();
	}

	@Override
	protected Object initValue() {
		return null;
	}

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:comboBox id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                    label=\"" + this.getLabel() + "\"");
        xhtmlCodeExample.appendInnerContent("                    value=\"" + this.getValue() + "\"");
        xhtmlCodeExample.appendInnerContent("                    tooltip=\"" + this.getTooltip() + "\"");
        xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + this.getStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                    inputStyleClass=\"" + this.getInputStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                    labelStyleClass=\"" + this.getLabelStyleClass() + "\"");
        xhtmlCodeExample.appendInnerContent("                    readonly=\"" + this.isReadonly() + "\"");
        xhtmlCodeExample.appendInnerContent("                    required=\"" + this.isRequired() + "\"");
        xhtmlCodeExample.appendInnerContent("                    filterable=\"" + filterable + "\"");
        xhtmlCodeExample.appendInnerContent("                    autoFocus=\"" + this.isAutoFocus() + "\"");
        xhtmlCodeExample.appendInnerContent("                    rendered=\"" + this.isRendered() + "\">");

        this.addAjaxTag(xhtmlCodeExample, "keyup");

        if (this.comboBoxValueType == ComboBoxValueType.STRING) {
            xhtmlCodeExample.appendInnerContent("            <f:selectItem itemValue=\"#{null}\"");
            xhtmlCodeExample.appendInnerContent("                          itemLabel=\"Choose one...\"/>");
            xhtmlCodeExample.appendInnerContent("            <f:selectItem itemValue=\"2000\"");
            xhtmlCodeExample.appendInnerContent("                          itemLabel=\"Year 2000\"/>");
            xhtmlCodeExample.appendInnerContent("            <f:selectItem itemValue=\"2010\"");
            xhtmlCodeExample.appendInnerContent("                          itemLabel=\"Year 2010\"/>");
            xhtmlCodeExample.appendInnerContent("            <f:selectItem itemValue=\"2020\"");
            xhtmlCodeExample.appendInnerContent("                          itemLabel=\"Year 2020\"/>");
        } else if (this.comboBoxValueType == ComboBoxValueType.ENUM) {
            xhtmlCodeExample.appendInnerContent("            <f:selectItems value=\"#{bean.fooEnums}\"/>");
        } else if (this.comboBoxValueType == ComboBoxValueType.OBJECT) {
            xhtmlCodeExample.appendInnerContent("            <f:selectItems value=\"#{bean.fooObjects}\"/>");
            xhtmlCodeExample.appendInnerContent("            <f:converter converterId=\"fooConverter\"/>    ");
        }

        xhtmlCodeExample.appendInnerContent("        </b:comboBox>", false);

        this.addOutputExample(xhtmlCodeExample);

        codeExamples.add(xhtmlCodeExample);

		 generateDemoCSS(codeExamples);
    }

	@Override
	public Object getValue() {
		if (super.getValue() != null) {
			return super.getValue().toString();
		}

		return "(item is null)";
	}

	@Override
	public String getReadableValue() {
		if (super.getValue() != null) {
			if (super.getValue() instanceof Foo) {
				return ((Foo) super.getValue()).getValue();
			} else if (super.getValue() instanceof FooType) {
				return ((FooType) super.getValue()).label;
			}

			return (String) super.getValue();
		}

		return "(item is null)";
	}

	public List<SelectItem> getValues() {
		switch (this.comboBoxValueType) {
		case OBJECT:
			return this.foos;
		case ENUM:
			return this.enums;
		default:
			return this.strings;
		}
	}

	public boolean isConverterActive() {
		return this.comboBoxValueType == ComboBoxValueType.OBJECT;
	}

	public List<SelectItem> getComboBoxTypes() {
		final List<SelectItem> items = new ArrayList<SelectItem>();

		for (final ComboBoxValueType type : ComboBoxValueType.values()) {
			items.add(new SelectItem(type, type.label));
		}
		return items;
	}

	public ComboBoxValueType getComboBoxValueType() {
		return this.comboBoxValueType;
	}

	public void setComboBoxValueType(final ComboBoxValueType comboBoxValueType) {
		this.comboBoxValueType = comboBoxValueType;
	}

	public boolean getFilterable() {
		return filterable;
	}

	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
	}

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    private void initFoos() {
		this.foos.add(new SelectItem(null, "Choose one..."));

		for (final String key : FooConverter.fooMap.keySet()) {
			final Foo foo = FooConverter.fooMap.get(key);
			this.foos.add(new SelectItem(foo, foo.getKey()));
		}
	}

	private void initEnums() {
		this.enums.add(new SelectItem(null, "Choose one..."));

		for (final FooType fooType : FooType.values()) {
			this.enums.add(new SelectItem(fooType.label));
		}
	}

	private void initStrings() {
		this.strings.add(new SelectItem(null, "Choose one..."));
		this.strings.add(new SelectItem("2000", "Year 2000"));
		this.strings.add(new SelectItem("2010", "Year 2010"));
		this.strings.add(new SelectItem("2020", "Year 2020"));
	}
}
