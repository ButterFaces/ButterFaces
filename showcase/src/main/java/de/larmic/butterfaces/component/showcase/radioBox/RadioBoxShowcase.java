package de.larmic.butterfaces.component.showcase.radioBox;

import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.radioBox.examples.RadioBoxListOfStringsMyBeanExample;
import de.larmic.butterfaces.util.StringUtils;
import de.larmic.butterfaces.component.showcase.comboBox.Foo;
import de.larmic.butterfaces.component.showcase.comboBox.FooConverter;
import de.larmic.butterfaces.component.showcase.comboBox.FooType;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.type.ComboBoxValueType;
import de.larmic.butterfaces.component.showcase.type.RadioBoxLayoutType;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class RadioBoxShowcase extends AbstractInputShowcase implements Serializable {

	private ComboBoxValueType comboBoxValueType = ComboBoxValueType.STRING;
    private RadioBoxLayoutType radioBoxLayoutType = RadioBoxLayoutType.LINE_DIRECTION;

	private final List<Foo> foos = new ArrayList<>();
	private final List<FooType> enums = new ArrayList<>();
	private final List<String> strings = new ArrayList<>();

	public RadioBoxShowcase() {
		this.initFoos();
		this.initStrings();
		this.initEnums();
	}

	@Override
	protected Object initValue() {
		return null;
	}

	@Override
	public Object getValue() {
		if (super.getValue() != null) {
			return super.getValue();
		}

		return "(item is null)";
	}

	@Override
	public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
		final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

		xhtmlCodeExample.appendInnerContent("        <b:radioBox id=\"input\"");
		xhtmlCodeExample.appendInnerContent("                    label=\"" + this.getLabel() + "\"");
		xhtmlCodeExample.appendInnerContent("                    hideLabel=\"" + isHideLabel() + "\"");
		xhtmlCodeExample.appendInnerContent("                    value=\"#{myBean.selectedValue}\"");
		xhtmlCodeExample.appendInnerContent("                    values=\"#{myBean.values}\"");
		xhtmlCodeExample.appendInnerContent("                    styleClass=\"" + this.getStyleClass() + "\"");
		xhtmlCodeExample.appendInnerContent("                    readonly=\"" + this.isReadonly() + "\"");
		xhtmlCodeExample.appendInnerContent("                    required=\"" + this.isRequired() + "\"");
		xhtmlCodeExample.appendInnerContent("                    disabled=\"" + this.isDisabled() + "\"");
		xhtmlCodeExample.appendInnerContent("                    layout=\"" + radioBoxLayoutType.label + "\"");
		xhtmlCodeExample.appendInnerContent("                    rendered=\"" + this.isRendered() + "\">");

		this.addAjaxTag(xhtmlCodeExample, "change");

		if (StringUtils.isNotEmpty(getTooltip())) {
			xhtmlCodeExample.appendInnerContent("            <b:tooltip>");
			xhtmlCodeExample.appendInnerContent("                " + getTooltip());
			xhtmlCodeExample.appendInnerContent("            </b:tooltip>");
		}

		xhtmlCodeExample.appendInnerContent("        </b:radioBox>", false);

		this.addOutputExample(xhtmlCodeExample);

		codeExamples.add(xhtmlCodeExample);

        if (ComboBoxValueType.STRING.equals(this.comboBoxValueType)) {
            codeExamples.add(new RadioBoxListOfStringsMyBeanExample());
        }

		generateDemoCSS(codeExamples);
	}


	@Override
	public String getReadableValue() {
		if (super.getValue() != null) {
			if (super.getValue() instanceof Foo) {
				return ((Foo) super.getValue()).getValue();
			} else if (super.getValue() instanceof FooType) {
				return ((FooType) super.getValue()).getLabel();
			} else if (super.getValue() instanceof SelectItem) {
                return ((SelectItem) super.getValue()).getLabel();
            }

			return (String) super.getValue();
		}

		return "(item is null)";
	}

	public List getValues() {
		switch (this.comboBoxValueType) {
		case OBJECT:
			return this.foos;
		case ENUM:
			return this.enums;
		default:
			return this.strings;
		}
	}

	public List<SelectItem> getComboBoxTypes() {
		final List<SelectItem> items = new ArrayList<>();

		for (final ComboBoxValueType type : ComboBoxValueType.values()) {
            if (!ComboBoxValueType.TEMPLATE.equals(type)) {
                items.add(new SelectItem(type, type.label));
            }
		}
		return items;
	}

    public List<SelectItem> getRadioLayoutTypes() {
		final List<SelectItem> items = new ArrayList<>();

		for (final RadioBoxLayoutType type : RadioBoxLayoutType.values()) {
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

    public RadioBoxLayoutType getRadioBoxLayoutType() {
        return radioBoxLayoutType;
    }

    public void setRadioBoxLayoutType(RadioBoxLayoutType radioBoxLayoutType) {
        this.radioBoxLayoutType = radioBoxLayoutType;
    }

    private void initFoos() {
		for (final String key : FooConverter.fooMap.keySet()) {
			final Foo foo = FooConverter.fooMap.get(key);
			this.foos.add(foo);
		}
	}

	private void initEnums() {
		for (final FooType fooType : FooType.values()) {
			this.enums.add(fooType);
		}
	}

	private void initStrings() {
		this.strings.add("Year 2000");
		this.strings.add("Year 2010");
		this.strings.add("Year 2020");
	}
}
