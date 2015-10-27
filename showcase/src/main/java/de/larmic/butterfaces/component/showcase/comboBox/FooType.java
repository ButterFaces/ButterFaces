package de.larmic.butterfaces.component.showcase.comboBox;

public enum FooType {
	FOO_TYPE_1("FooTypeEnumLabel1"),
	FOO_TYPE_2("FooTypeEnumLabel2"),
	FOO_TYPE_3("FooTypeEnumLabel3");

	public String label;

	FooType(final String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
