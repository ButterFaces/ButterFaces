package de.larmic.butterfaces.component.showcase.radioBox;

import de.larmic.butterfaces.component.showcase.AbstractInputShowcase;
import de.larmic.butterfaces.component.showcase.example.AbstractCodeExample;
import de.larmic.butterfaces.component.showcase.example.XhtmlCodeExample;
import de.larmic.butterfaces.component.showcase.radioBox.examples.*;
import de.larmic.butterfaces.component.showcase.type.RadioBoxExampleType;
import de.larmic.butterfaces.component.showcase.type.RadioBoxLayoutType;
import de.larmic.butterfaces.util.StringUtils;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class RadioBoxShowcase extends AbstractInputShowcase implements Serializable {

	private RadioBoxExampleType exampleType = RadioBoxExampleType.STRING;
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
		//xhtmlCodeExample.appendInnerContent("                    layout=\"" + radioBoxLayoutType.label + "\"");
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

        if (RadioBoxExampleType.STRING.equals(this.exampleType)) {
            codeExamples.add(new RadioBoxListOfStringsMyBeanExample());
        } else if (RadioBoxExampleType.ENUM.equals(this.exampleType)) {
            codeExamples.add(new RadioBoxListOfEnumsMyBeanExample());
            codeExamples.add(new RadioBoxFooTypeExample());
        } else if (RadioBoxExampleType.OBJECT.equals(this.exampleType)) {
            codeExamples.add(new RadioBoxListOfObjectsMyBeanExample());
            codeExamples.add(new RadioBoxFooExample());
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
		switch (this.exampleType) {
		case OBJECT:
			return this.foos;
		case ENUM:
			return this.enums;
		default:
			return this.strings;
		}
	}

	public List<SelectItem> getExampleTypes() {
		final List<SelectItem> items = new ArrayList<>();

		for (final RadioBoxExampleType type : RadioBoxExampleType.values()) {
            if (!RadioBoxExampleType.TEMPLATE.equals(type)) {
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

	public RadioBoxExampleType getExampleType() {
		return this.exampleType;
	}

	public void setExampleType(final RadioBoxExampleType exampleType) {
		this.exampleType = exampleType;
	}

    public RadioBoxLayoutType getRadioBoxLayoutType() {
        return radioBoxLayoutType;
    }

    public void setRadioBoxLayoutType(RadioBoxLayoutType radioBoxLayoutType) {
        this.radioBoxLayoutType = radioBoxLayoutType;
    }

    private void initFoos() {
        this.foos.add(new Foo("fooKey1", "fooValue1"));
        this.foos.add(new Foo("fooKey2", "fooValue2"));
        this.foos.add(new Foo("fooKey3", "fooValue3"));
	}

	private void initEnums() {
        Collections.addAll(this.enums, FooType.values());
	}

	private void initStrings() {
		this.strings.add("Year 2000");
		this.strings.add("Year 2010");
		this.strings.add("Year 2020");
	}
}
